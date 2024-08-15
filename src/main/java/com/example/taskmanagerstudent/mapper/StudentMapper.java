package com.example.taskmanagerstudent.mapper;

import com.example.taskmanagerstudent.dto.request.StudentDto;
import com.example.taskmanagerstudent.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = {CourseMapper.class})
public interface StudentMapper {
    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

    Student toEntity(StudentDto studentDto);

    @Mapping(target = "courseDtos", ignore = true)
    StudentDto toDto(Student student);

    List<Student> STUDENT_LIST(List<StudentDto> studentList);

    List<StudentDto> DTO_LIST(List<Student> studentList);

    void updateStudentByStudentDto(StudentDto studentDto, @MappingTarget Student student);

    void updateEntityFromDto(StudentDto studentDto, @MappingTarget Student student);
}
