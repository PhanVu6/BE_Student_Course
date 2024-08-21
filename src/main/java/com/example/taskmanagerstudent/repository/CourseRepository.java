package com.example.taskmanagerstudent.repository;

import com.example.taskmanagerstudent.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query(value = "from Course c " +
            "where (:title is null or c.title like %:title%)")
    Page<Course> findAllCourse(@Param("title") String title,
                               Pageable pageable);
}
