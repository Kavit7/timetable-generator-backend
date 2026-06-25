package com.timetable_generator.Kavit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.timetable_generator.Kavit.model.School;
import com.timetable_generator.Kavit.model.SchoolSettings;



@Repository

public interface SchoolSettingsRepository extends JpaRepository <SchoolSettings,Long>{

    public Optional<SchoolSettings> findBySchool(School school);
 
    
} 
