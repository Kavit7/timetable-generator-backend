package com.timetable_generator.Kavit.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.timetable_generator.Kavit.dto.UserDto;
import com.timetable_generator.Kavit.model.School;
import com.timetable_generator.Kavit.model.User;
import com.timetable_generator.Kavit.serviceimpl.SchoolServiceImpl;
import com.timetable_generator.Kavit.serviceimpl.UserServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor

public class UserController {


    private final UserServiceImpl userService;
    private final SchoolServiceImpl schoolService;
 
    @GetMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody UserDto userDto) {
          
             School school = schoolService.createSchool(userDto.getSchoolName());
            
             User user = new User();
             user.setEmail(userDto.getEmail());
             user.setName(userDto.getName());
             user.setPassword(userDto.getPassword());
             user.setSchool(school);
             return ResponseEntity.ok(user);
    }
    
}
