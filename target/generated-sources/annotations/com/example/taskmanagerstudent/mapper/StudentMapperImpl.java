package com.example.taskmanagerstudent.mapper;

import com.example.taskmanagerstudent.dto.request.StudentDto;
import com.example.taskmanagerstudent.entity.Student;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-15T00:50:44+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.3 (Amazon.com Inc.)"
)
public class StudentMapperImpl implements StudentMapper {

    @Override
    public Student toEntity(StudentDto studentDto) {
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

    @Override
    public StudentDto toDto(Student student) {
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

    @Override
    public List<Student> STUDENT_LIST(List<StudentDto> studentList) {
        if ( studentList == null ) {
            return null;
        }

        List<Student> list = new ArrayList<Student>( studentList.size() );
        for ( StudentDto studentDto : studentList ) {
            list.add( toEntity( studentDto ) );
        }

        return list;
    }

    @Override
    public List<StudentDto> DTO_LIST(List<Student> studentList) {
        if ( studentList == null ) {
            return null;
        }

        List<StudentDto> list = new ArrayList<StudentDto>( studentList.size() );
        for ( Student student : studentList ) {
            list.add( toDto( student ) );
        }

        return list;
    }

    @Override
    public void updateStudentByStudentDto(StudentDto studentDto, Student student) {
        if ( studentDto == null ) {
            return;
        }

        student.setId( studentDto.getId() );
        student.setName( studentDto.getName() );
        student.setEmail( studentDto.getEmail() );
        student.setStatus( studentDto.getStatus() );
    }

    @Override
    public void updateEntityFromDto(StudentDto studentDto, Student student) {
        if ( studentDto == null ) {
            return;
        }

        student.setId( studentDto.getId() );
        student.setName( studentDto.getName() );
        student.setEmail( studentDto.getEmail() );
        student.setStatus( studentDto.getStatus() );
    }
}
