package com.timetable_generator.Kavit.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.timetable_generator.Kavit.dto.SessionDto;
import com.timetable_generator.Kavit.response.CountResponse;
import com.timetable_generator.Kavit.serviceimpl.SessionArrangeServiceImpl;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
public class HelloController {
  private final SessionArrangeServiceImpl sessionArrangeServiceImpl;
//     @PostMapping("/hello")
//    public ResponseEntity<CountResponse> counter(@RequestBody SessionDto request){
//    sessionArrangeServiceImpl.simulateSession(request);
//     return ResponseEntity.ok(new CountResponse());
    
//    }
    
}
