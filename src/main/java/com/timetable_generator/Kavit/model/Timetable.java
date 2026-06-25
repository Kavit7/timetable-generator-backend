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
@Table(name ="timetable")
@NoArgsConstructor
@AllArgsConstructor
public class Timetable {
    
       @Id 
       @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "timeslot_id")
    private Timeslot timeslot;
    @ManyToOne
    @JoinColumn(name = "timetable_group_id")
    private TimetableGroup timetableGroup;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "subject_id")
     private Subject subject;
    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;
}
