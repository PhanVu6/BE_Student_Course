package com.example.taskmanagerstudent.service;

import com.example.taskmanagerstudent.dto.request.CourseDto;
import com.example.taskmanagerstudent.dto.request.StudentDto;
import com.example.taskmanagerstudent.dto.request.Student_CourseDto;
import com.example.taskmanagerstudent.dto.response.ApiResponse;
import com.example.taskmanagerstudent.entity.Course;
import com.example.taskmanagerstudent.entity.Student;
import com.example.taskmanagerstudent.mapper.CourseMapper;
import com.example.taskmanagerstudent.mapper.StudentMapper;
import com.example.taskmanagerstudent.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private StudentCourseService studentCourseService;
    private CourseMapper courseMapper = CourseMapper.INSTANCE;
    private StudentMapper studentMapper = StudentMapper.INSTANCE;

    public ApiResponse<List<CourseDto>> getAll() {
        List<Course> courseList = courseRepository.findAll();
        List<CourseDto> result = courseMapper.DTO_LIST(courseList);

        ApiResponse<List<CourseDto>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(result);
        apiResponse.setMessage(result != null ? "Thành công lấy danh sách course" : "Thất bại lấy danh sách course");
        return apiResponse;
    }


    public ApiResponse<CourseDto> getOne(Long id) {
        Course course = courseRepository.findById(id).get();
        CourseDto result = courseMapper.toDto(course);
        List<Student> students = course.getStudent_courses().stream()
                .filter(filterById -> filterById.getCourse().getId() == id)
                .map(addStudent -> addStudent.getStudent())
                .collect(Collectors.toList());

        result.setStudentDtos(studentMapper.DTO_LIST(students));

        ApiResponse<CourseDto> apiResponse = new ApiResponse<>();
        apiResponse.setResult(result);
        apiResponse.setMessage(result != null ? "Thành công lấy course" : "Thất bại lấy course");
        return apiResponse;
    }

    @Transactional
    public ApiResponse<CourseDto> update(CourseDto courseDto, Long id) {
        ApiResponse<CourseDto> apiResponse = new ApiResponse<>();
        if (!courseRepository.existsById(id)) {
            apiResponse.setMessage("Course ko tồn tại");
            return null;
        }

        if (courseDto != null) {
            Course course = courseRepository.findById(id).get();
            course = courseMapper.toEntity(courseDto);
            course.setId(id);
            CourseDto result = courseMapper.toDto(course);

            apiResponse.setResult(result);
            apiResponse.setMessage(result != null ? "Thành công update Course" : "Thất bại update Course");
        }

        return apiResponse;
    }

    @Transactional
    public ApiResponse<CourseDto> updateStudentsByCourse(StudentDto studentDto, Long courseId) {
        Course course = courseRepository.findById(courseId).get();
        course.setId(courseId);

        Student_CourseDto contain = new Student_CourseDto();
        Student student = studentMapper.toEntity(studentDto);
        contain.setCourseDto(courseMapper.toDto(course));
        contain.setStudentDto(studentMapper.toDto(student));

        studentCourseService.create(contain);

        return getOne(courseId);
    }

    @Transactional
    public ApiResponse<CourseDto> save(CourseDto courseDto) {
        Course course = courseMapper.toEntity(courseDto);
        course = courseRepository.save(course);
        CourseDto result = courseMapper.toDto(course);

        ApiResponse<CourseDto> apiResponse = new ApiResponse<>();
        apiResponse.setResult(result);
        apiResponse.setMessage(result != null ? "Thành công tạo course" : "Thất bại tạo course");
        return apiResponse;
    }

    @Transactional
    public ApiResponse<Boolean> delete(Long id) {
        ApiResponse<Boolean> apiResponse = new ApiResponse<>();
        if (!courseRepository.existsById(id)) {
            apiResponse.setMessage("Course ko tồn tại");
            apiResponse.setResult(false);
            return apiResponse;
        }

        courseRepository.deleteById(id);

        apiResponse.setMessage("Course đã xóa");
        apiResponse.setResult(true);
        return apiResponse;
    }
}
