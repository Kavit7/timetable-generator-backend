package com.timetable_generator.Kavit.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.timetable_generator.Kavit.model.ClassTable;
import com.timetable_generator.Kavit.model.School;
import com.timetable_generator.Kavit.model.Subject;
import com.timetable_generator.Kavit.model.Teacher;
import com.timetable_generator.Kavit.model.TeacherSubjectClass;

@Repository
public interface TeacherSubjectClassRepository extends JpaRepository <TeacherSubjectClass,Long> {

    Optional<TeacherSubjectClass> findByClassTableAndSubjectAndTeacherAndSchool( ClassTable ct, Subject subject,Teacher teacher, School school);
@Query("""
    SELECT COUNT(DISTINCT t.teacher.id)
    FROM TeacherSubjectClass t
    WHERE t.subject = :subject
    AND t.school = :school
""") 
long countDistinctTeachersBySubjectAndSchool(Subject subject,School school);
    @Query(" SELECT COUNT (DISTINCT t.classTable.id) FROM TeacherSubjectClass t where t.subject =:subject and t.school=:school")
   long countBySubjectPerClass(Subject subject,School school);


    @Query("SELECT DISTINCT tsc.teacher FROM TeacherSubjectClass tsc WHERE tsc.school = :school")
    List<Teacher> findDistinctTeachersBySchool(School school);
    List<TeacherSubjectClass> findBySchool(School school);
} 
