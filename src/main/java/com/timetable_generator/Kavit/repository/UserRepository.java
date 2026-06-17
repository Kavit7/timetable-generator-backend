package com.timetable_generator.Kavit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.timetable_generator.Kavit.model.User;

@Repository

public interface UserRepository  extends JpaRepository<User, Long> {
    

    public Optional<User> findByEmail(String email);
}
