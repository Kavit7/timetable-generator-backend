package com.timetable_generator.Kavit.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.timetable_generator.Kavit.model.ClassTable;
import com.timetable_generator.Kavit.model.School;
import com.timetable_generator.Kavit.model.Subject;
import com.timetable_generator.Kavit.model.Teacher;
import com.timetable_generator.Kavit.model.TeacherSubjectClass;

@Repository
public interface TeacherSubjectClassRepository extends JpaRepository <TeacherSubjectClass,Long> {

    Optional<TeacherSubjectClass> findByClassTableAndSubjectAndTeacherAndSchool( ClassTable ct, Subject subject,Teacher teacher, School school);

   
} 
