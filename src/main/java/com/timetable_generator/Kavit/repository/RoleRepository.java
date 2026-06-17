package com.timetable_generator.Kavit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.timetable_generator.Kavit.model.Role;


@Repository

public interface RoleRepository extends JpaRepository<Role, Long> {
    
}
