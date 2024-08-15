package com.example.taskmanagerstudent.repository;

<<<<<<< HEAD
import com.example.taskmanagerstudent.entity.Student_Course;
=======
import com.example.taskmanagerstudent.entity.StudentCourse;
>>>>>>> store-temp
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

<<<<<<< HEAD
public interface StudentCourseRepository extends JpaRepository<Student_Course, Long> {
    @Query(
            value = "from Student_Course sc " +
                    "where :studentId = sc.student.id"
    )
    List<Student_Course> findByStudentId(@Param("studentId") Long studentId);

    List<Student_Course> findByStudentIdInAndCourseIdIn(Set<Long> studentIds, Set<Long> courseIds);

    @Query(value = "FROM Student_Course sc WHERE sc.student.id = :studentId and sc.course.id = :courseId")
    Optional<Student_Course> findByStudentAndCourse(
            @Param("studentId") Long studentId,
            @Param("courseId") Long courseId);

    @Query("SELECT sc.course.id FROM Student_Course sc WHERE sc.student.id = :studentId")
    List<Long> findByStudentIdAndCourseIds(@Param("studentId") Long studentId);

    @Modifying
    @Query("UPDATE Student_Course sc SET sc.status = :status WHERE sc.student.id = :studentId AND sc.course.id IN :courseIds")
=======
public interface StudentCourseRepository extends JpaRepository<StudentCourse, Long> {
    @Query(
            value = "select sc " +
                    "from StudentCourse sc " +
                    "join fetch sc.student s " +
                    "join fetch sc.course c " +
                    "where s.id = :studentId"
    )
    List<StudentCourse> findByStudentId(@Param("studentId") Long studentId);


    List<StudentCourse> findByStudentIdInAndCourseIdIn(Set<Long> studentIds, Set<Long> courseIds);

    @Query(value = "FROM StudentCourse sc WHERE sc.student.id = :studentId and sc.course.id = :courseId")
    Optional<StudentCourse> findByStudentAndCourse(
            @Param("studentId") Long studentId,
            @Param("courseId") Long courseId);

    @Query("SELECT sc.course.id FROM StudentCourse sc WHERE sc.student.id = :studentId")
    List<Long> findByStudentIdAndCourseIds(@Param("studentId") Long studentId);

    @Modifying
    @Query("UPDATE StudentCourse sc SET sc.status = :status WHERE sc.student.id = :studentId AND sc.course.id IN :courseIds")
>>>>>>> store-temp
    void changeStatusByStudentIdAndCourseIds(@Param("studentId") Long studentId,
                                             @Param("courseIds") Set<Long> courseIds,
                                             @Param("status") String status);


}
