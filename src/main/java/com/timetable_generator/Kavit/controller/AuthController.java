package com.timetable_generator.Kavit.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.timetable_generator.Kavit.dto.LoginRequest;
import com.timetable_generator.Kavit.serviceimpl.AuthServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {


     private final AuthServiceImpl authServiceImpl;
  @PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequest request) {

    try {
        return ResponseEntity.ok(Map.of(
    "message", "Logged successfully",
    "data", authServiceImpl.login(request)
));

    } catch (RuntimeException e) {

        return ResponseEntity.status(401).body(
            Map.of(
                "error", true,
                "message", e.getMessage()
            )
        );
    }
}
    
}
