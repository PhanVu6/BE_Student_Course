package com.example.taskmanagerstudent.repository;

import com.example.taskmanagerstudent.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import static com.example.taskmanagerstudent.repository.common.StudentSQL.*;

public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query(value = valueSearchStudent)
    Page<Student> searchStudent(
            @Param("name") String name,
            Pageable pageable);


    @Query(value = valueSearchByStudentCourses,
            countQuery = countSearchByStudentCourses,
            nativeQuery = true)
    Page<Object[]> searchByStudentCourses(
            @Param("name") String name,
            Pageable pageable);


    @Query(valueGetStudentById)
    List<Object[]> getStudentById(@Param("id") Long id);


    @Query(value = valueGetStudentAndCourses,
            nativeQuery = true)
    List<Object[]> getStudentAndCourses(@Param("id") Long id);

    @Query(value = valueSearchStudentAndTitleCourses,
            countQuery = countSearchStudentAndTitleCourses,
            nativeQuery = true)
    Page<Object[]> searchStudentAndTitleCourses(@Param("name") String name, Pageable pageable);

}
