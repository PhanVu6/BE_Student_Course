package com.example.taskmanagerstudent.repository;

import com.example.taskmanagerstudent.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query(value = "from Course c")
    Page<Course> findAllCourse(Pageable pageable);
}
