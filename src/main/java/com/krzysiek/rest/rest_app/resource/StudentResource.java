package com.krzysiek.rest.rest_app.resource;

import com.krzysiek.rest.rest_app.entity.Student;
import com.krzysiek.rest.rest_app.repository.IStudentRepository;
import com.krzysiek.rest.rest_app.exception.ElementNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.Resource;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RestController
public class StudentResource {

    @Autowired
    private IStudentRepository studentRepository;

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/students")
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @GetMapping("/students/{id}")
    public Resource<Student> getStudentById(@PathVariable Long id) {
        Optional<Student> studentOptional = studentRepository.findById(id);
        checkStudentExists(id, studentOptional);
        Resource<Student> resource = createLinkToAllStudents(studentOptional.get());
        return resource;
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
        resource.add(linkToAllStudents.withRel(messageSource.getMessage("all.students.link", null, LocaleContextHolder.getLocale())));
        return resource;
    }

}
