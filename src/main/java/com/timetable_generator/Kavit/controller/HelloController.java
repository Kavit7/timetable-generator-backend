package com.timetable_generator.Kavit.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloController {

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello, World! My name is avit paulo";
    }
    
}
