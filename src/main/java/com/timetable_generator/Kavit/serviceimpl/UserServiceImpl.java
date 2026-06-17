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
        @Override
        public User createUser(User user) {
           user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
           return userRepository.save(user);
        }
    
}
