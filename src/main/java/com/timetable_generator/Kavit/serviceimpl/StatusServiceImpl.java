package com.timetable_generator.Kavit.serviceimpl;

import org.springframework.stereotype.Service;

import com.timetable_generator.Kavit.model.Status;
import com.timetable_generator.Kavit.repository.StatusRepository;
import com.timetable_generator.Kavit.services.StatusService;

import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
public class StatusServiceImpl implements StatusService {

    private final StatusRepository statusRepository;

    @Override
    public Status createStatus(Status status) {
        return null;
    }

    @Override
    public String getStatusName() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStatusName'");
    }

    @Override
    public Status findByName(String status) {
        Status stat=statusRepository.findByName(status);
        return stat;
    }
    
}
