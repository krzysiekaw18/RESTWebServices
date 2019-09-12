package com.krzysiek.rest.rest_app.repository;

import com.krzysiek.rest.rest_app.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISubjectRepository extends JpaRepository<Subject, Long> {
}
