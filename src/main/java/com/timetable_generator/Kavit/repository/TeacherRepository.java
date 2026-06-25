package com.timetable_generator.Kavit.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.timetable_generator.Kavit.model.School;
import com.timetable_generator.Kavit.model.Teacher;



@Repository

public interface TeacherRepository extends JpaRepository <Teacher,Long> {
    
   public Optional<Teacher> findByNameIgnoreCaseAndSchool(String name, School school);

   public List<Teacher> findBySchool(School school);

   public Teacher findTeacherById(long teacherId);
}
