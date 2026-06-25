package com.timetable_generator.Kavit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.timetable_generator.Kavit.model.School;
import com.timetable_generator.Kavit.model.Timeslot;



@Repository

public interface TimeslotRepository extends JpaRepository<Timeslot,Long> {
    public Timeslot findTimeslotById(Long id);

    public List<Timeslot> findBySchool(School school);
   
    public void deleteBySchool(School school);
    
    public long countBySchoolAndIsBreak(School school , boolean is_break);

    public List<Timeslot> findBySchoolAndIsBreak(School school, boolean b);

    public List<Timeslot> findAllBySchool(School school);
} 