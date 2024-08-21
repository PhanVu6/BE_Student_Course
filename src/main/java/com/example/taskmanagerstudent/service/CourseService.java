package com.example.taskmanagerstudent.service;

import com.example.taskmanagerstudent.dto.request.CourseDto;
import com.example.taskmanagerstudent.dto.response.ApiResponse;
import com.example.taskmanagerstudent.entity.Course;
import com.example.taskmanagerstudent.exception.AppException;
import com.example.taskmanagerstudent.exception.ErrorCode;
import com.example.taskmanagerstudent.mapper.CourseMapper;
import com.example.taskmanagerstudent.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final MessageSource messageSource;
    private final CourseRepository courseRepository;

    private final StudentCourseService studentCourseService;
    private CourseMapper courseMapper = CourseMapper.INSTANCE;

    public ApiResponse<Page<CourseDto>> getAll(String title, int number, int size) {
        Pageable pageable = PageRequest.of(number, size);
        Page<Course> courseList = courseRepository.findAllCourse(title, pageable);
        List<CourseDto> courseDtos = courseMapper.DTO_LIST(courseList.getContent());
        Page<CourseDto> results = new PageImpl<>(courseDtos, pageable, courseList.getTotalElements());

        ApiResponse<Page<CourseDto>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(results);
        apiResponse.setMessage(results.getTotalElements() != 0 ?
                messageSource.getMessage("success.get.all", null, LocaleContextHolder.getLocale())
                : messageSource.getMessage("error.get.not.found", null, LocaleContextHolder.getLocale()));
        return apiResponse;
    }


    public ApiResponse<CourseDto> getOne(Long id) {
        ApiResponse<CourseDto> apiResponse = new ApiResponse<>();
        apiResponse.setMessage(messageSource.getMessage("error.operation", null, LocaleContextHolder.getLocale()));

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
        CourseDto result = courseMapper.toDto(course);

        apiResponse.setResult(result);
        apiResponse.setMessage(messageSource.getMessage("success.get.all", null, LocaleContextHolder.getLocale()));
        return apiResponse;
    }

    @Transactional
    public ApiResponse<CourseDto> update(CourseDto courseDto, Long id) {
        ApiResponse<CourseDto> apiResponse = new ApiResponse<>();
        apiResponse.setMessage(messageSource.getMessage("error.operation", null, LocaleContextHolder.getLocale()));

        if (courseDto != null) {
            Course course = courseRepository.findById(id)
                    .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
            course = courseMapper.toEntity(courseDto);
            course.setId(id);
            course = courseRepository.save(course);
            CourseDto result = courseMapper.toDto(course);

            apiResponse.setResult(result);
            apiResponse.setMessage(messageSource.getMessage("success.update", null, LocaleContextHolder.getLocale()));
        }

        return apiResponse;
    }

//    @Transactional
//    public ApiResponse<CourseDto> updateStudentsByCourse(StudentDto studentDto, Long courseId) {
//        Course course = courseRepository.findById(courseId).get();
//        course.setId(courseId);
//
//        Student_CourseDto contain = new Student_CourseDto();
//        Student student = studentMapper.toEntity(studentDto);
//        contain.setCourseDto(courseMapper.toDto(course));
//        contain.setStudentDto(studentMapper.toDto(student));
//
//        studentCourseService.create(contain);
//
//        return getOne(courseId);
//    }

    @Transactional
    public ApiResponse<CourseDto> save(CourseDto courseDto) {
        ApiResponse<CourseDto> apiResponse = new ApiResponse<>();
        apiResponse.setMessage(messageSource.getMessage("error.operation", null, LocaleContextHolder.getLocale()));

        Course course = courseMapper.toEntity(courseDto);
        course = courseRepository.save(course);
        CourseDto result = courseMapper.toDto(course);

        apiResponse.setResult(result);
        apiResponse.setMessage(messageSource.getMessage("success.create", null, LocaleContextHolder.getLocale()));
        return apiResponse;
    }


    public ApiResponse<Boolean> deleteTempCourse(Long courseId, String status) {
        ApiResponse<Boolean> apiResponse = new ApiResponse<>();
        apiResponse.setResult(false);
        apiResponse.setMessage(messageSource.getMessage("error.operation", null, LocaleContextHolder.getLocale()));

        if (!courseRepository.existsById(courseId)) {
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);
        }

        try {
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
            course.setId(courseId);
            course.setStatus(status);
            course = courseRepository.save(course);
            apiResponse.setResult(true);
            apiResponse.setMessage(messageSource.getMessage("success.operation", null, LocaleContextHolder.getLocale()));
            return apiResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return apiResponse;
        }
    }

    @Transactional
    public ApiResponse<Boolean> delete(Long id) {
        ApiResponse<Boolean> apiResponse = new ApiResponse<>();
        apiResponse.setResult(false);
        apiResponse.setMessage(messageSource.getMessage("error.operation", null, LocaleContextHolder.getLocale()));

        if (!courseRepository.existsById(id)) {
            throw new AppException(ErrorCode.STUDENT_NOT_FOUND);
        }

        courseRepository.deleteById(id);

        apiResponse.setMessage(messageSource.getMessage("success.operation", null, LocaleContextHolder.getLocale()));
        apiResponse.setResult(true);
        return apiResponse;
    }
}
