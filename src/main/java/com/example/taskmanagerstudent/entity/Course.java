package com.example.taskmanagerstudent.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "course")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", length = 200)
    private String title;

    @Column(name = "description")
    private String description;


    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<Student_Course> student_courses;
}
