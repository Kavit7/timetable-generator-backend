package com.timetable_generator.Kavit.serviceimpl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.timetable_generator.Kavit.config.JwtService;
import com.timetable_generator.Kavit.dto.LoginRequest;
import com.timetable_generator.Kavit.model.User;
import com.timetable_generator.Kavit.repository.UserRepository;
import com.timetable_generator.Kavit.response.AuthResponse;

import lombok.Data;
import lombok.RequiredArgsConstructor;


@Service

@Data
@RequiredArgsConstructor
public class AuthServiceImpl {



    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;


    public AuthResponse login(LoginRequest request){
        

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));



        User user= userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new RuntimeException("User not found"));
    


        Map<String ,Object> claims = new HashMap<>();
        claims.put("role",user.getRole().getName());
        claims.put("school_id",user.getSchool().getId());


        String token= jwtService.generateToken(claims, user);

        return new AuthResponse(token);


    }
    
}
