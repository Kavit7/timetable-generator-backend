package com.timetable_generator.Kavit.model;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import jakarta.annotation.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;


    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;

    @Column(name = "reset_token")
    private String resetToken;
    

    @Column(name = "reset_token_expiry")
    private LocalDateTime resetTokenExpiry;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        
        return List.of(new SimpleGrantedAuthority(this.role.getName()));
    }


    @Override
    public String getUsername() {
        return this.email;
    }


    @Override
    public @Nullable String getPassword() {
        return this.password;
    }




}
