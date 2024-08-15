package com.example.taskmanagerstudent.repository.common;

public class StudentSQL {
    public static final String valueSearchStudent =
            "select distinct s " +
                    "from Student s " +
                    "left join fetch s.studentCourse sc " +
                    "left join fetch sc.course c " +
                    "where (:name is null or s.name like %:name%) " +
                    "and (sc.status is null or sc.status = '1') " +
                    "order by s.id";
    public static final String valueSearchByStudentCourses =
            "SELECT DISTINCT s.id, s.name, s.email, s.status, " +
                    "c.id as courseId, c.title as courseTitle, c.description as courseDescription, c.status as courseStatus " +
                    "FROM Student s " +
                    "LEFT JOIN student_course sc ON s.id = sc.student_id " +
                    "AND sc.status = '1' " +
                    "LEFT JOIN Course c ON sc.course_id = c.id " +
                    "WHERE :name IS NULL OR s.name LIKE %:name% " +
                    "ORDER BY s.id";
    public static final String countSearchByStudentCourses =
            "select count(distinct s.id) " +
                    "from Student s " +
                    "LEFT JOIN Student_Course sc ON s.id = sc.student_id AND sc.status = '1' " +
                    "WHERE :name IS NULL OR s.name LIKE %:name% ";

    public static final String valueGetStudentById =
            "select s, c from Student s " +
                    "left join fetch s.studentCourse sc " +
                    "left join fetch sc.course c " +
                    "where s.id = :id and sc.status = '1' ";

    public static final String valueGetStudentAndCourses =
            "SELECT s.*, c.* FROM student s " +
                    "LEFT JOIN student_course sc ON s.id = sc.student_id " +
                    "and sc.status = '1' " +
                    "LEFT JOIN course c ON sc.course_id = c.id " +
                    "WHERE s.id = :id";

    public static final String valueSearchStudentAndTitleCourses =
            "SELECT s.*, " +
                    "GROUP_CONCAT(c.title SEPARATOR ', ') AS courses " +
                    "FROM student s " +
                    "LEFT JOIN student_course sc ON s.id = sc.student_id " +
                    "and sc.status LIKE 1 " +
                    "LEFT JOIN course c ON sc.course_id = c.id " +
                    "WHERE :name is null or s.name LIKE %:name% " +
                    "GROUP BY s.id";

    public static final String countSearchStudentAndTitleCourses =
            "SELECT COUNT(DISTINCT s.id) " +
                    "FROM student s " +
                    "LEFT JOIN student_course sc ON s.id = sc.student_id " +
                    "and sc.status LIKE 1 " +
                    "LEFT JOIN course c ON sc.course_id = c.id " +
                    "WHERE :name is null or s.name LIKE %:name%";
}
