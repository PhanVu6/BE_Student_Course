package com.example.taskmanagerstudent.repository;

import com.example.taskmanagerstudent.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query(value = "select distinct s, c " +
            "from Student s " +
            "left join fetch s.student_courses sc " +
            "left join fetch sc.course c " +
            "where :name is null or s.name like %:name% " +
            "order by s.id ",
            countQuery = "select count(distinct s) " +
                    "from Student s " +
                    "left join s.student_courses sc " +
                    "left join sc.course c " +
                    "where :name is null or s.name like %:name% " +
                    "order by s.id ")
    Page<Object[]> searchStudent(
            @Param("name") String name,
            Pageable pageable);

    @Query(value = "SELECT s.id, s.name, s.email, s.status, " +
            "c.id as courseId, c.title as courseTitle, c.description as courseDescription, c.status as courseStatus " +
            "FROM Student s " +
            "LEFT JOIN Student_Course sc ON s.id = sc.student_id " +
            "LEFT JOIN Course c ON sc.course_id = c.id " +
            "WHERE :name IS NULL OR s.name LIKE %:name% " +
            "ORDER BY s.id",
            countQuery = "SELECT COUNT(DISTINCT s.id) " +
                    "FROM Student s " +
                    "LEFT JOIN Student_Course sc ON s.id = sc.student_id " +
                    "LEFT JOIN Course c ON sc.course_id = c.id " +
                    "WHERE :name IS NULL OR s.name LIKE %:name%",
            nativeQuery = true)
    Page<Object[]> searchByStudentCourses(
            @Param("name") String name,
            Pageable pageable);

    @Query("select s, c from Student s " +
            "left join fetch s.student_courses sc " +
            "left join fetch sc.course c " +
            "where s.id = :id")
    List<Object[]> getStudentById(@Param("id") Long id);


    @Query(value = "SELECT s.*, c.* FROM student s " +
            "LEFT JOIN student_course sc ON s.id = sc.student_id " +
            "LEFT JOIN course c ON sc.course_id = c.id " +
            "WHERE s.id = :id",
            nativeQuery = true)
    List<Object[]> getStudentAndCourses(@Param("id") Long id);

    @Query(value = "SELECT s.*, " +
            "GROUP_CONCAT(c.title SEPARATOR ', ') AS courses " +
            "FROM student s " +
            "LEFT JOIN student_course sc ON s.id = sc.student_id and sc.status LIKE 1 " +
            "LEFT JOIN course c ON sc.course_id = c.id " +
            "WHERE :name is null or s.name LIKE %:name% " +
            "GROUP BY s.id",
            countQuery = "SELECT COUNT(DISTINCT s.id) " +
                    "FROM student s " +
                    "LEFT JOIN student_course sc ON s.id = sc.student_id " +
                    "LEFT JOIN course c ON sc.course_id = c.id " +
                    "WHERE :name is null or s.name LIKE %:name%",
            nativeQuery = true)
    Page<Object[]> searchStudentAndTitleCourses(@Param("name") String name, Pageable pageable);

}
