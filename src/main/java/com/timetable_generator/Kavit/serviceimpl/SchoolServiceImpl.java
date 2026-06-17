package com.timetable_generator.Kavit.serviceimpl;

import org.springframework.stereotype.Service;

import com.timetable_generator.Kavit.model.School;
import com.timetable_generator.Kavit.repository.SchoolRepository;
import com.timetable_generator.Kavit.services.SchoolService;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class SchoolServiceImpl implements SchoolService {

    private final SchoolRepository schoolRepository;

    @Override
    public School createSchool(String schoolName) {

        School school = new School();
        school.setName(schoolName);
        schoolRepository.save(school);
        return school;
    }
    
    
}
