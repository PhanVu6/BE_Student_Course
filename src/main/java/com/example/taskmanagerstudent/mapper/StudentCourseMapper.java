package com.example.taskmanagerstudent.mapper;

import com.example.taskmanagerstudent.dto.request.Student_CourseDto;
import com.example.taskmanagerstudent.entity.Student_Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface StudentCourseMapper {
    StudentCourseMapper INSTANCE = Mappers.getMapper(StudentCourseMapper.class);

    @Mapping(target = "student", source = "studentDto")
    @Mapping(target = "course", source = "courseDto")
    Student_Course toEntity(Student_CourseDto studentCourseDto);

    @Mapping(target = "studentDto", source = "student")
    @Mapping(target = "courseDto", source = "course")
    Student_CourseDto toDto(Student_Course studentCourse);

    List<Student_Course> toListEntity(List<Student_CourseDto> studentCourseDto);

    List<Student_CourseDto> DTO_LIST(List<Student_Course> student_courses);
}
