package com.example.taskmanagerstudent.repository;

import com.example.taskmanagerstudent.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
