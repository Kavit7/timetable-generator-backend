package com.timetable_generator.Kavit.serviceimpl;

import org.springframework.stereotype.Service;

import com.timetable_generator.Kavit.model.Role;
import com.timetable_generator.Kavit.repository.RoleRepository;
import com.timetable_generator.Kavit.services.RoleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class RoleServiceImpl implements RoleService{
     private final RoleRepository roleRepository;
    @Override
    public Role createRole(Role role) {
       return null;
    }

@Override
public Role findByName(String Name) {
   Role role=  roleRepository.findByName(Name);
   return role;
} 
    
}
