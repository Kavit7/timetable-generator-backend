package com.timetable_generator.Kavit.services;

import com.timetable_generator.Kavit.model.Role;

public interface RoleService {
    
    public Role createRole(Role role); 


    public Role findByName(String Name);
}
