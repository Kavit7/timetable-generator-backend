package com.timetable_generator.Kavit.services;

import java.util.List;

import com.timetable_generator.Kavit.dto.TimeslotGenerationRequest;
import com.timetable_generator.Kavit.model.Timeslot;

public interface TimeslotService {



    public List<Timeslot> generateTimeslots(TimeslotGenerationRequest request);
    
}
