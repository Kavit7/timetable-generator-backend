package com.timetable_generator.Kavit.dto;

import com.timetable_generator.Kavit.model.Role;
import com.timetable_generator.Kavit.model.School;
import com.timetable_generator.Kavit.model.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String schoolName;

    
}
