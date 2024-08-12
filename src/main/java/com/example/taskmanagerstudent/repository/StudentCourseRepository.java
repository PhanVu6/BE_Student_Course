package com.example.taskmanagerstudent.repository;

import com.example.taskmanagerstudent.entity.Course;
import com.example.taskmanagerstudent.entity.Student;
import com.example.taskmanagerstudent.entity.Student_Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentCourseRepository extends JpaRepository<Student_Course, Long> {
    List<Student_Course> findByStudentId(Long studentId);

    @Query(value = "FROM Student_Course sc WHERE sc.student.id = :studentId and sc.course.id = :courseId")
    Optional<Student_Course> findByStudentAndCourse(
            @Param("studentId") Long studentId,
            @Param("courseId") Long courseId);

    @Query(value = "FROM Student_Course sc WHERE sc.student = :student and sc.course = :course")
    Optional<Student_Course> findByStudentAndCourse(
            @Param("student") Student studentId,
            @Param("course") Course courseId);

//    @Query(value = "")
//    Page<Object[]> findStudent
}
