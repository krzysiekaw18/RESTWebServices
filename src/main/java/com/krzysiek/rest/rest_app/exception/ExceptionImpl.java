package com.krzysiek.rest.rest_app.exception;

import com.krzysiek.rest.rest_app.entity.Student;
import com.krzysiek.rest.rest_app.entity.Subject;
import com.krzysiek.rest.rest_app.entity.Teacher;
import org.apache.naming.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;

import java.util.Optional;

public class ExceptionImpl {

    public static void checkStudentExists(Long id, Optional<Student> optional) {
        if (!optional.isPresent()) {
            throw new ElementNotFoundException("Student with id: " + id + " not exists");
        }
    }

    public static void checkSubjectExists(String subjectName, Optional<Subject> optional) {
        if (!optional.isPresent()) {
            throw new ElementNotFoundException("Subject: " + subjectName + " not exists");
        }
    }

    public static void checkSubjectExists(Long id, Optional<Subject> optional) {
        if (!optional.isPresent()) {
            throw new ElementNotFoundException("Subject with id: " + id + " not exists");
        }
    }

    public static void checkSubjectExistsOnStudentList(String subjectName, Optional<Subject> optional) {
        if (!optional.isPresent()) {
            throw new ElementNotFoundException("Student's subjects list not contains subject: " + subjectName);
        }
    }

    public static void checkTeacherExists(Long id, Optional<Teacher> optional) {
        if (!optional.isPresent()) {
            throw new ElementNotFoundException("Teacher with id: " + id + " not exists");
        }
    }


}
