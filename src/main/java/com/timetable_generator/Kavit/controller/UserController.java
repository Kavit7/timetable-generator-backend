package com.timetable_generator.Kavit.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.timetable_generator.Kavit.dto.SessionDto;
import com.timetable_generator.Kavit.dto.UserDto;
import com.timetable_generator.Kavit.model.School;
import com.timetable_generator.Kavit.model.User;
import com.timetable_generator.Kavit.response.CountResponse;
import com.timetable_generator.Kavit.serviceimpl.SchoolServiceImpl;
import com.timetable_generator.Kavit.serviceimpl.SessionArrangeServiceImpl;
import com.timetable_generator.Kavit.serviceimpl.UserServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor

public class UserController {


    private final UserServiceImpl userService;
    private final SchoolServiceImpl schoolService;
     private final SessionArrangeServiceImpl sessionArrangeServiceImpl;
    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto) {
          try{
       School school = schoolService.createSchool(userDto.getSchoolName());

             User user = new User();
             user.setEmail(userDto.getEmail());
             user.setName(userDto.getFullName());
             user.setPassword(userDto.getPassword());
             user.setSchool(school);
             userService.createUser(user);
             return ResponseEntity.ok(Map.of("success",true,"message","Account created Suucessfully"));
          }
          catch( Exception e){
            return ResponseEntity.status(401).body(Map.of("error",true,"message",e.getMessage()));
          }
          
    }

   
 
    
}
