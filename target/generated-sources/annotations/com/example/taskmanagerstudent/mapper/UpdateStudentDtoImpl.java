package com.example.taskmanagerstudent.mapper;

import com.example.taskmanagerstudent.dto.request.StudentDto;
import com.example.taskmanagerstudent.dto.request.UpdateStudentCourseDto;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-15T00:50:44+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.3 (Amazon.com Inc.)"
)
public class UpdateStudentDtoImpl implements UpdateStudentDto {

    @Override
    public StudentDto toStudentDto(UpdateStudentCourseDto updateStudentCourseDto) {
        if ( updateStudentCourseDto == null ) {
            return null;
        }

        StudentDto.StudentDtoBuilder studentDto = StudentDto.builder();

        studentDto.id( updateStudentCourseDto.getStudentId() );
        studentDto.name( updateStudentCourseDto.getName() );
        studentDto.email( updateStudentCourseDto.getEmail() );
        studentDto.status( updateStudentCourseDto.getStatus() );

        return studentDto.build();
    }
}
