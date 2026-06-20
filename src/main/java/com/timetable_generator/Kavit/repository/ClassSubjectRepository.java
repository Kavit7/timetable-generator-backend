package com.timetable_generator.Kavit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.timetable_generator.Kavit.model.ClassSubject;
import com.timetable_generator.Kavit.model.ClassTable;

import com.timetable_generator.Kavit.model.Subject;



@Repository

public interface ClassSubjectRepository extends JpaRepository<ClassSubject, Long>{

    Optional<ClassSubject> findByClassTableAndSubject (ClassTable ct, Subject sc);
    
}
