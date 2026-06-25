package com.timetable_generator.Kavit.response;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountResponse {

    private long slots;

    private Map<Long, Double> subjectWeight;

    private Map<Long, Long> classCount;

    private Map<Long, Long> teacherCount;
}