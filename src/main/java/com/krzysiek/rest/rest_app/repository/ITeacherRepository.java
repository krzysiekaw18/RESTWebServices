package com.krzysiek.rest.rest_app.repository;

import com.krzysiek.rest.rest_app.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITeacherRepository extends JpaRepository<Teacher, Long> {
}
