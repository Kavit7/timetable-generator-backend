package com.timetable_generator.Kavit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.timetable_generator.Kavit.model.School;
import com.timetable_generator.Kavit.model.TimetableGroup;



@Repository
public interface TimetableGroupRepository extends JpaRepository<TimetableGroup,Long> {

    void deleteBySchool(School school);

    TimetableGroup findTimetableGroupById(Long id);

    List<TimetableGroup> findBySchool(School school);
    
}
