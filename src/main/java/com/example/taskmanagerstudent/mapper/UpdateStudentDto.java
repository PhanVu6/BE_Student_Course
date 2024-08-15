package com.example.taskmanagerstudent.mapper;

import com.example.taskmanagerstudent.dto.request.StudentDto;
import com.example.taskmanagerstudent.dto.request.UpdateStudentCourseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UpdateStudentDto {
    UpdateStudentDto INSTANCE = Mappers.getMapper(UpdateStudentDto.class);

    @Mapping(target = "id", source = "studentId")
    StudentDto toStudentDto(UpdateStudentCourseDto updateStudentCourseDto);
}
