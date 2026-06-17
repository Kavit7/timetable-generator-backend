package com.timetable_generator.Kavit.services;

import com.timetable_generator.Kavit.model.Status;

public interface StatusService {
    public Status createStatus(Status status);
    public String getStatusName();
    public Status findByName(String status);
}
