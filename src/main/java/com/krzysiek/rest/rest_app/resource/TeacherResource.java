package com.krzysiek.rest.rest_app.resource;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.krzysiek.rest.rest_app.entity.Student;
import com.krzysiek.rest.rest_app.entity.Subject;
import com.krzysiek.rest.rest_app.entity.Teacher;
import com.krzysiek.rest.rest_app.repository.ITeacherRepository;
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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.krzysiek.rest.rest_app.exception.ExceptionImpl.checkTeacherExists;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/teachers")
public class TeacherResource {

    @Autowired
    private ITeacherRepository teacherRepository;

    @Autowired
    private MessageSource messageSource;

    @GetMapping
    public MappingJacksonValue getAllTeachers() {
        List<Resource<Teacher>> teachersList = teacherRepository.findAll().stream()
                .map(this::createTeacherResource)
                .collect(Collectors.toList());
        SimpleBeanPropertyFilter teachersFilter = SimpleBeanPropertyFilter.filterOutAllExcept("firstName", "secondName");
        return getObjectMappingJacksonValue("TeacherFilter", teachersList, teachersFilter);
    }

    @GetMapping("/{id}")
    public MappingJacksonValue getTeacherById(@PathVariable Long id) {
        Optional<Teacher> teacherOptional = teacherRepository.findById(id);
        checkTeacherExists(id, teacherOptional);
        Teacher teacher = teacherOptional.get();

        Resource<Teacher> resource = new Resource<>(teacher);
        Link allTeachersLink = linkTo(methodOn(this.getClass()).getAllTeachers()).withRel(getTranslateMessage("all.teachers.link"));
        Link teachersStudentLink = linkTo(methodOn(this.getClass()).getTeacherStudents(teacher.getId())).withRel(getTranslateMessage("all.teachers.students.link"));
        resource.add(teachersStudentLink, allTeachersLink);
        SimpleBeanPropertyFilter teacherFilter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "firstName", "secondName", "subjects");
        return getObjectMappingJacksonValue("TeacherFilter", resource, teacherFilter);
    }

    @GetMapping("/{id}/students")
    public MappingJacksonValue getTeacherStudents(@PathVariable Long id) {
        Optional<Teacher> teacherOptional = teacherRepository.findById(id);
        checkTeacherExists(id, teacherOptional);
        Teacher teacher = teacherOptional.get();

        Set<Resource<Student>> students = new HashSet<>();
        teacher.getSubjects().stream()
                .map(Subject::getStudents)
                .forEach(studentsList -> studentsList.forEach(student ->
                        students.add(createStudentResource(student))
                ));
        SimpleBeanPropertyFilter studentFilter = SimpleBeanPropertyFilter.filterOutAllExcept("firstName", "secondName", "yearOfStudy");
        return getObjectMappingJacksonValue("StudentsFilter", students, studentFilter);
    }

    @PostMapping
    public ResponseEntity<Object> addNewTeacher(@Valid @RequestBody Teacher teacher) {
        teacherRepository.save(teacher);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                .path("{id}")
                .buildAndExpand(teacher.getId())
                .toUri())
                .build();
    }

    private String getTranslateMessage(String message) {
        return messageSource.getMessage(message, null, LocaleContextHolder.getLocale());
    }

    private Resource<Teacher> createTeacherResource(Teacher teacher) {
        Link teacherDetailsLink = linkTo(methodOn(this.getClass()).getTeacherById(teacher.getId())).withRel(getTranslateMessage("teacher.details.link"));
        return new Resource<>(teacher, teacherDetailsLink);
    }

    private Resource<Student> createStudentResource(Student student) {
        Link studentDetailsLink = linkTo(methodOn(StudentResource.class).getStudentById(student.getId())).withRel(getTranslateMessage("student.details.link"));
        return new Resource<>(student, studentDetailsLink);
    }

    private MappingJacksonValue getObjectMappingJacksonValue(String filterName, Object resource, SimpleBeanPropertyFilter filter) {
        FilterProvider filterProvider = new SimpleFilterProvider().addFilter(filterName, filter);
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(resource);
        mappingJacksonValue.setFilters(filterProvider);
        return mappingJacksonValue;
    }


}
