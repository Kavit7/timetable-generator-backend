package com.timetable_generator.Kavit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.timetable_generator.Kavit.model.Status;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {

    public Status findByName(String status);
    
}
