package com.timetable_generator.Kavit.serviceimpl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.timetable_generator.Kavit.model.User;

import com.timetable_generator.Kavit.repository.UserRepository;
import com.timetable_generator.Kavit.services.UserService;

import lombok.RequiredArgsConstructor;

@Service

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {



    private final UserRepository userRepository;
    private final StatusServiceImpl statusServiceImpl;
    private final RoleServiceImpl roleServiceImpl;
        @Override
        public User createUser(User user) {
           user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

           if (user.getRole() == null || user.getStatus() == null){
            user.setRole(roleServiceImpl.findByName("staff"));
            user.setStatus(statusServiceImpl.findByName("active"));
           }
           return userRepository.save(user);
        }
    
}
