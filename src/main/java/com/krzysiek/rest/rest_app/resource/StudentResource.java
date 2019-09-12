package com.krzysiek.rest.rest_app.resource;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.krzysiek.rest.rest_app.entity.Student;
import com.krzysiek.rest.rest_app.entity.Subject;
import com.krzysiek.rest.rest_app.repository.IStudentRepository;
import com.krzysiek.rest.rest_app.exception.ElementNotFoundException;
import com.krzysiek.rest.rest_app.repository.ISubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/students")
public class StudentResource {

    @Autowired
    private IStudentRepository studentRepository;

    @Autowired
    private ISubjectRepository subjectRepository;

    @Autowired
    private MessageSource messageSource;

    @GetMapping()
    public MappingJacksonValue getAllStudents() {
        List<Resource<Student>> studentResources = studentRepository.findAll().stream()
                .map(this::createStudentResource)
                .collect(Collectors.toList());
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("firstName", "secondName", "yearOfStudy");
        return getStudentMappingJacksonValue(studentResources, filter);
    }

    @GetMapping("/{id}")
    public MappingJacksonValue getStudentById(@PathVariable Long id) {
        Optional<Student> studentOptional = studentRepository.findById(id);
        checkStudentExists(id, studentOptional);
        Resource<Student> resource = createLinkForStudent(studentOptional.get());
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id","firstName", "secondName", "yearOfStudy", "birthday", "email");
        return getStudentMappingJacksonValue(resource, filter);
    }

    @GetMapping("/{id}/subjects")
    public Set<Subject> getAllSubjectsForStudent(@PathVariable Long id) {
        Optional<Student> studentOptional = studentRepository.findById(id);
        checkStudentExists(id, studentOptional);
        return studentOptional.get().getSubjects();
    }

    @PostMapping()
    public ResponseEntity<Object> addNewStudent(@Valid @RequestBody Student newStudent) {
        studentRepository.save(newStudent);

        //change status 200(ok) -> 201(Created)
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newStudent.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

//    @PostMapping("/id/subjects")
//    public ResponseEntity<Object> addNewSubjectToStudent(@PathVariable Long id, @RequestBody Subject subject) {
//        Optional<Student> studentOptional = studentRepository.findById(id);
//        checkStudentExists(id, studentOptional);
////        subject.getStudents().add(studentOptional.get());
//        studentOptional.get().getSubjects().add(subject);
////        subjectRepository.save(subject);
//        studentRepository.save(studentOptional.get());
//        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
//                .path("/{id}")
//                .buildAndExpand(subject.getId())
//                .toUri();
//        return ResponseEntity.created(location).build();
//    }

    // another possibility is 204 status (No content) return
    @DeleteMapping("/{id}")
    public void deleteStudentById(@PathVariable Long id) {
        studentRepository.deleteById(id);
    }

    private void checkStudentExists(@PathVariable Long id, Optional<Student> studentOptional) {
        if (!studentOptional.isPresent()) {
            throw new ElementNotFoundException("Student with id: " + id + " not exists");
        }
    }

    //    HATEOAS
    private Resource<Student> createLinkForStudent(Student student) {
        Resource<Student> resource = new Resource<>(student);
        Link studentSubjects = linkTo(methodOn(this.getClass()).getAllSubjectsForStudent(student.getId())).withRel(getTranslateMessage("student.subjects.link"));
        Link linkToAllStudents = linkTo(methodOn(this.getClass()).getAllStudents()).withRel(getTranslateMessage("all.students.link"));
        resource.add(studentSubjects, linkToAllStudents);
        return resource;
    }

    private Resource<Student> createStudentResource(Student student) {
        Link studentDetailsLink = linkTo(methodOn(this.getClass()).getStudentById(student.getId())).withRel(getTranslateMessage("student.details.link"));
        return new Resource<>(student, studentDetailsLink);
    }

    private String getTranslateMessage(String message) {
        return messageSource.getMessage(message, null, LocaleContextHolder.getLocale());
    }

    private MappingJacksonValue getStudentMappingJacksonValue(Object resource, SimpleBeanPropertyFilter filter) {
        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("StudentsFilter", filter);
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(resource);
        mappingJacksonValue.setFilters(filterProvider);
        return mappingJacksonValue;
    }

}
