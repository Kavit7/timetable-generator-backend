package com.timetable_generator.Kavit.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="teacher_availability")
public class TeacherAvailability {
    


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;
    @ManyToOne
    @JoinColumn(name="teacher_id")
    private Teacher teacher;
    @ManyToOne
    @JoinColumn(name="timeslot_id")
    private Timeslot timeslot;
    @Column(name="is_available")
    private boolean isAvailable=true;
    @ManyToOne
    @JoinColumn(name ="school_id")
    private School school;

}
