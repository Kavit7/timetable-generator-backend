package com.timetable_generator.Kavit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.timetable_generator.Kavit.model.School;
import com.timetable_generator.Kavit.model.Subject;




@Repository
public interface SubjectRepository extends JpaRepository <Subject, Long>{
    public Optional<Subject> findByNameIgnoreCaseAndSchool(String name, School school);
}
