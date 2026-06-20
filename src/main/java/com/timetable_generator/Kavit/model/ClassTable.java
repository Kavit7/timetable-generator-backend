package com.timetable_generator.Kavit.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "class_table")
public class ClassTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String defaultRoomName;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;
}