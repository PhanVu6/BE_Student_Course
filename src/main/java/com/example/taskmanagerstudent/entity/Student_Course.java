package com.example.taskmanagerstudent.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "student_course")
@Getter
@Setter
public class Student_Course {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "status")
    @Pattern(regexp = "0|1", message = "Status phải là 0 hoặc 1")
    private String status;
}
