package com.timetable_generator.Kavit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.timetable_generator.Kavit.model.ClassTable;
import com.timetable_generator.Kavit.model.School;



@Repository
public interface ClassTableRepository extends JpaRepository<ClassTable,Long> {
      public Optional<ClassTable> findByNameIgnoreCaseAndSchool(String name,School school);
}
