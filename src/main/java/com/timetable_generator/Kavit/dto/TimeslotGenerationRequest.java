package com.timetable_generator.Kavit.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class TimeslotGenerationRequest {


  
    private Long schoolId;
 
    // e.g. ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday"]
    private List<String> selectedDays;
 
    private String morningStart;   // "08:00"
    private String morningEnd;     // "12:00"
    private String eveningStart;   // "13:00"
    private String eveningEnd;     // "16:00"
 
    private Integer periodLength;   // minutes
    private Integer breakAfter;     // insert a short break after this many periods
    private Integer breakDuration;  // minutes
 
    // Only relevant if "Friday" is in selectedDays. Must fall fully
    // inside [eveningStart, eveningEnd] — validated again on the backend.
    private String fridayReligiousStart;
    private String fridayReligiousEnd;


    
}
