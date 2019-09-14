package com.krzysiek.rest.rest_app.repository;

import com.krzysiek.rest.rest_app.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ISubjectRepository extends JpaRepository<Subject, Long> {

    Optional<Subject> findSubjectByName(String subjectName);

}
