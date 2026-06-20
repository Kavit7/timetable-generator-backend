package com.timetable_generator.Kavit.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "teacher_subject_class"
    // uniqueConstraints = @UniqueConstraint(
    //     columnNames = {"teacher_id", "subject_id", "class_id", "school_id"}
    // )
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherSubjectClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "class_id")   // ✅ correct
   private ClassTable classTable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;
}
