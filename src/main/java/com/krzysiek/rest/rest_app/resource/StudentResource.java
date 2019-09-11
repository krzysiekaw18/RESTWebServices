package com.krzysiek.rest.rest_app.resource;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.krzysiek.rest.rest_app.entity.Student;
import com.krzysiek.rest.rest_app.repository.IStudentRepository;
import com.krzysiek.rest.rest_app.exception.ElementNotFoundException;
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
import java.util.stream.Collectors;

@RestController
public class StudentResource {

    @Autowired
    private IStudentRepository studentRepository;

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/students")
    public MappingJacksonValue getAllStudents() {
        List<Resource<Student>> studentResources = studentRepository.findAll().stream()
                .map(this::createStudentResource)
                .collect(Collectors.toList());
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("firstName", "secondName", "yearOfStudy");
        return getStudentMappingJacksonValue(studentResources, filter);
    }

    @GetMapping("/students/{id}")
    public MappingJacksonValue getStudentById(@PathVariable Long id) {
        Optional<Student> studentOptional = studentRepository.findById(id);
        checkStudentExists(id, studentOptional);
        Resource<Student> resource = createLinkToAllStudents(studentOptional.get());
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("id","firstName", "secondName", "yearOfStudy", "birthday", "email");
        return getStudentMappingJacksonValue(resource, filter);
    }

    @PostMapping("/students")
    public ResponseEntity<Object> addNewStudent(@Valid @RequestBody Student newStudent) {
        studentRepository.save(newStudent);

        //change status 200(ok) -> 201(Created)
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newStudent.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    // another possibility is 204 status (No content) return
    @DeleteMapping("/students/{id}")
    public void deleteStudentById(@PathVariable Long id) {
        studentRepository.deleteById(id);
    }

    private void checkStudentExists(@PathVariable Long id, Optional<Student> studentOptional) {
        if (!studentOptional.isPresent()) {
            throw new ElementNotFoundException("Student with id: " + id + " not exists");
        }
    }

    //    HATEOAS
    private Resource<Student> createLinkToAllStudents(Student student) {
        Resource<Student> resource = new Resource<>(student);
        ControllerLinkBuilder linkToAllStudents = linkTo(methodOn(this.getClass()).getAllStudents());
        resource.add(linkToAllStudents.withRel(getTranslateMessage("all.students.link")));
        return resource;
    }

    private Resource<Student> createStudentResource(Student student) {
        Link studentDetailsLink = linkTo(methodOn(this.getClass()).getStudentById(student.getId())).withRel(getTranslateMessage("details.students.link"));
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
