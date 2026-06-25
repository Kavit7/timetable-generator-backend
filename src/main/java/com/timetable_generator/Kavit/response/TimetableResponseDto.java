package com.timetable_generator.Kavit.response;

import com.timetable_generator.Kavit.model.DayOfWeekEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
    public class TimetableResponseDto {


        private String className;       // timetable_group.name
        private DayOfWeekEnum day;
        private String startTime;
        private String endTime;
        private String subjectName;
        private String teacherName;
        private Boolean isBreak;
        private Long timeslotId;
        private Long timetableGroupId;
        
    }
