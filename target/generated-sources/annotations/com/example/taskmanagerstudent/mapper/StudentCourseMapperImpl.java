package com.example.taskmanagerstudent.mapper;

import com.example.taskmanagerstudent.dto.request.CourseDto;
import com.example.taskmanagerstudent.dto.request.StudentDto;
import com.example.taskmanagerstudent.dto.request.Student_CourseDto;
import com.example.taskmanagerstudent.entity.Course;
import com.example.taskmanagerstudent.entity.Student;
import com.example.taskmanagerstudent.entity.StudentCourse;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-15T00:50:44+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.3 (Amazon.com Inc.)"
)
public class StudentCourseMapperImpl implements StudentCourseMapper {

    @Override
    public StudentCourse toEntity(Student_CourseDto studentCourseDto) {
        if ( studentCourseDto == null ) {
            return null;
        }

        StudentCourse studentCourse = new StudentCourse();

        studentCourse.setStudent( studentDtoToStudent( studentCourseDto.getStudentDto() ) );
        studentCourse.setCourse( courseDtoToCourse( studentCourseDto.getCourseDto() ) );
        studentCourse.setId( studentCourseDto.getId() );
        studentCourse.setStatus( studentCourseDto.getStatus() );

        return studentCourse;
    }

    @Override
    public Student_CourseDto toDto(StudentCourse studentCourse) {
        if ( studentCourse == null ) {
            return null;
        }

        Student_CourseDto.Student_CourseDtoBuilder student_CourseDto = Student_CourseDto.builder();

        student_CourseDto.studentDto( studentToStudentDto( studentCourse.getStudent() ) );
        student_CourseDto.courseDto( courseToCourseDto( studentCourse.getCourse() ) );
        student_CourseDto.id( studentCourse.getId() );
        student_CourseDto.status( studentCourse.getStatus() );

        return student_CourseDto.build();
    }

    @Override
    public List<StudentCourse> toListEntity(List<Student_CourseDto> studentCourseDto) {
        if ( studentCourseDto == null ) {
            return null;
        }

        List<StudentCourse> list = new ArrayList<StudentCourse>( studentCourseDto.size() );
        for ( Student_CourseDto student_CourseDto : studentCourseDto ) {
            list.add( toEntity( student_CourseDto ) );
        }

        return list;
    }

    @Override
    public List<Student_CourseDto> DTO_LIST(List<StudentCourse> student_cours) {
        if ( student_cours == null ) {
            return null;
        }

        List<Student_CourseDto> list = new ArrayList<Student_CourseDto>( student_cours.size() );
        for ( StudentCourse studentCourse : student_cours ) {
            list.add( toDto( studentCourse ) );
        }

        return list;
    }

    protected Student studentDtoToStudent(StudentDto studentDto) {
        if ( studentDto == null ) {
            return null;
        }

        Student student = new Student();

        student.setId( studentDto.getId() );
        student.setName( studentDto.getName() );
        student.setEmail( studentDto.getEmail() );
        student.setStatus( studentDto.getStatus() );

        return student;
    }

    protected Course courseDtoToCourse(CourseDto courseDto) {
        if ( courseDto == null ) {
            return null;
        }

        Course.CourseBuilder course = Course.builder();

        course.id( courseDto.getId() );
        course.title( courseDto.getTitle() );
        course.description( courseDto.getDescription() );
        course.status( courseDto.getStatus() );

        return course.build();
    }

    protected StudentDto studentToStudentDto(Student student) {
        if ( student == null ) {
            return null;
        }

        StudentDto.StudentDtoBuilder studentDto = StudentDto.builder();

        studentDto.id( student.getId() );
        studentDto.name( student.getName() );
        studentDto.email( student.getEmail() );
        studentDto.status( student.getStatus() );

        return studentDto.build();
    }

    protected CourseDto courseToCourseDto(Course course) {
        if ( course == null ) {
            return null;
        }

        CourseDto.CourseDtoBuilder courseDto = CourseDto.builder();

        courseDto.id( course.getId() );
        courseDto.title( course.getTitle() );
        courseDto.description( course.getDescription() );
        courseDto.status( course.getStatus() );

        return courseDto.build();
    }
}
