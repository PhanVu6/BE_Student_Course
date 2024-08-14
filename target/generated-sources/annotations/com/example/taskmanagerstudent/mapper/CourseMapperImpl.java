package com.example.taskmanagerstudent.mapper;

import com.example.taskmanagerstudent.dto.request.CourseDto;
import com.example.taskmanagerstudent.entity.Course;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-15T00:50:44+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.3 (Amazon.com Inc.)"
)
public class CourseMapperImpl implements CourseMapper {

    @Override
    public Course toEntity(CourseDto courseDto) {
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

    @Override
    public CourseDto toDto(Course course) {
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

    @Override
    public List<Course> COURSES_LIST(List<CourseDto> courseList) {
        if ( courseList == null ) {
            return null;
        }

        List<Course> list = new ArrayList<Course>( courseList.size() );
        for ( CourseDto courseDto : courseList ) {
            list.add( toEntity( courseDto ) );
        }

        return list;
    }

    @Override
    public List<CourseDto> DTO_LIST(List<Course> courseList) {
        if ( courseList == null ) {
            return null;
        }

        List<CourseDto> list = new ArrayList<CourseDto>( courseList.size() );
        for ( Course course : courseList ) {
            list.add( toDto( course ) );
        }

        return list;
    }

    @Override
    public void updateEntityFromDto(CourseDto courseDto, Course course) {
        if ( courseDto == null ) {
            return;
        }

        course.setId( courseDto.getId() );
        course.setTitle( courseDto.getTitle() );
        course.setDescription( courseDto.getDescription() );
        course.setStatus( courseDto.getStatus() );
    }
}
