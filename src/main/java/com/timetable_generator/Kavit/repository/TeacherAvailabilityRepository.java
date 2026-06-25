package com.timetable_generator.Kavit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.timetable_generator.Kavit.model.School;
import com.timetable_generator.Kavit.model.Teacher;
import com.timetable_generator.Kavit.model.TeacherAvailability;
import com.timetable_generator.Kavit.model.TeacherSubjectClass;
import com.timetable_generator.Kavit.model.Timeslot;



@Repository

public interface TeacherAvailabilityRepository extends JpaRepository <TeacherAvailability,Long> {

    boolean existsByTeacherAndTimeslotAndSchool(Teacher teacher, Timeslot slot, School school);

    void deleteBySchool(School school);

    List<TeacherAvailability> findBySchoolAndIsAvailableTrue(School school);

    
    
}
