package com.krzysiek.rest.rest_app.resource;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.krzysiek.rest.rest_app.entity.Student;
import com.krzysiek.rest.rest_app.entity.Subject;
import com.krzysiek.rest.rest_app.repository.IStudentRepository;
import com.krzysiek.rest.rest_app.repository.ISubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.krzysiek.rest.rest_app.exception.ExceptionImpl.checkSubjectExists;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

@RestController
@RequestMapping("/subjects")
public class SubjectResource {

    @Autowired
    private ISubjectRepository subjectRepository;

    @Autowired
    private IStudentRepository studentRepository;

    @Autowired
    private MessageSource messageSource;

    @GetMapping
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    @GetMapping("/{id}")
    public Resource<Subject> getSubjectById(@PathVariable Long id) {
        Optional<Subject> subjectOptional = subjectRepository.findById(id);
        checkSubjectExists(id, subjectOptional);
        Subject subject = subjectOptional.get();

        Resource<Subject> resource = new Resource<>(subject);
        Link allSubjectsLink = linkTo(methodOn(this.getClass()).getAllSubjects()).withRel(getTranslateMessage("all.subjects.link"));
        resource.add(allSubjectsLink);
        return resource;
    }

    @GetMapping("/{subjectName}/students")
    public MappingJacksonValue getAllStudentsForSubject(@PathVariable String subjectName) {
        Optional<Subject> subjectOptional = subjectRepository.findSubjectByName(subjectName);
        checkSubjectExists(subjectName, subjectOptional);
        Subject subject = subjectOptional.get();

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("firstName", "secondName", "yearOfStudy");
        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("StudentsFilter", filter);
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(subject.getStudents());
        mappingJacksonValue.setFilters(filterProvider);

        return mappingJacksonValue;
    }

    @PostMapping()
    public ResponseEntity<Object> addNewSubject(@Valid @RequestBody Subject newSubject) {
        subjectRepository.save(newSubject);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                .path("{id}")
                .buildAndExpand(newSubject.getId())
                .toUri())
                .build();
    }

    @DeleteMapping("/{id}")
    public void deleteSubjectById(@PathVariable Long id) {
//        it's not optimize solution but for a few records works
        studentRepository.findAll().forEach(student -> {
                    student.getSubjects().removeIf(subject -> subject.getId().equals(id));
                    studentRepository.save(student);
                }
        );
        subjectRepository.deleteById(id);
    }


    private String getTranslateMessage(String message) {
        return messageSource.getMessage(message, null, LocaleContextHolder.getLocale());
    }


}
