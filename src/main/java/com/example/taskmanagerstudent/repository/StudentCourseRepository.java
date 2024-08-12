package com.example.taskmanagerstudent.repository;

import com.example.taskmanagerstudent.entity.Student_Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface StudentCourseRepository extends JpaRepository<Student_Course, Long> {
    List<Student_Course> findByStudentId(Long studentId);

    @Query(value = "FROM Student_Course sc WHERE sc.student.id = :studentId and sc.course.id = :courseId")
    Optional<Student_Course> findByStudentAndCourse(
            @Param("studentId") Long studentId,
            @Param("courseId") Long courseId);

    @Query("SELECT sc FROM Student_Course sc WHERE sc.student.id = :studentId AND sc.course.id IN :courseIds")
    List<Student_Course> findByStudentIdAndCourseIds(@Param("studentId") Long studentId, @Param("courseIds") Set<Long> courseIds);
}
