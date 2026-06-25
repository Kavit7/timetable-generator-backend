package com.timetable_generator.Kavit.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.timetable_generator.Kavit.model.ClassSubject;
import com.timetable_generator.Kavit.model.ClassTable;
import com.timetable_generator.Kavit.model.School;
import com.timetable_generator.Kavit.model.Subject;



@Repository

public interface ClassSubjectRepository extends JpaRepository<ClassSubject, Long>{

    Optional<ClassSubject> findByClassTableAndSubject (ClassTable ct, Subject sc);

    List<ClassSubject> findAllBySubject_Id(Long id);

    ClassSubject findByClassTable_IdAndSubject_Id(Long id, Long id2);

     List<ClassSubject> findBySchool(School school);
    
}
