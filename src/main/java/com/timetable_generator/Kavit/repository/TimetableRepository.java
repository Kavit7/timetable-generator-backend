package com.timetable_generator.Kavit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.timetable_generator.Kavit.model.School;
import com.timetable_generator.Kavit.model.Timetable;


@Repository
public interface TimetableRepository extends JpaRepository<Timetable, Long> {

    void deleteBySchool(School school);

    List<Timetable> findBySchool(School school);
    
}
