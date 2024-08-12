package com.example.taskmanagerstudent.mapper;

import com.example.taskmanagerstudent.dto.request.CourseDto;
import com.example.taskmanagerstudent.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CourseMapper {
    CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);


    Course toEntity(CourseDto courseDto);

    @Mapping(target = "studentDtos", ignore = true)
    CourseDto toDto(Course course);

    List<Course> COURSES_LIST(List<CourseDto> courseList);

    List<CourseDto> DTO_LIST(List<Course> courseList);

    void updateEntityFromDto(CourseDto courseDto, @MappingTarget Course course);
}
