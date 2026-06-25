package com.timetable_generator.Kavit.model;

import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;





@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "timetable_group")
public class TimetableGroup {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;

    
}