package com.example.taskmanagerstudent.service;

import com.example.taskmanagerstudent.dto.request.CourseDto;
import com.example.taskmanagerstudent.dto.request.StudentDto;
import com.example.taskmanagerstudent.dto.request.Student_CourseDto;
import com.example.taskmanagerstudent.dto.response.ApiResponse;
import com.example.taskmanagerstudent.entity.Course;
import com.example.taskmanagerstudent.entity.Student;
import com.example.taskmanagerstudent.entity.Student_Course;
import com.example.taskmanagerstudent.mapper.CourseMapper;
import com.example.taskmanagerstudent.mapper.StudentCourseMapper;
import com.example.taskmanagerstudent.mapper.StudentMapper;
import com.example.taskmanagerstudent.repository.CourseRepository;
import com.example.taskmanagerstudent.repository.StudentCourseRepository;
import com.example.taskmanagerstudent.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentCourseService {
    @Autowired
    private StudentCourseRepository studentCourseRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseRepository courseRepository;
    private CourseMapper courseMapper = CourseMapper.INSTANCE;
    private StudentMapper studentMapper = StudentMapper.INSTANCE;
    private StudentCourseMapper studentCourseMapper = StudentCourseMapper.INSTANCE;

    @Transactional
    public Student_CourseDto create(Student_CourseDto studentCourseDto) {
        Student_Course result = studentCourseMapper.toEntity(studentCourseDto);
        result.setStatus("1");
        result = studentCourseRepository.save(result);
        return studentCourseMapper.toDto(result);
    }


    @Transactional
    public Student_CourseDto update(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with id: " + studentId));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + courseId));

        Student_Course result = new Student_Course();
        result.setStudent(student);
        result.setCourse(course);
        result.setStatus("1");

        result = studentCourseRepository.save(result);

        return studentCourseMapper.toDto(result);
    }

//    public StudentDto saveStudentWithManyCourse(){
//
//    }

    @Transactional
    public Student_CourseDto createStudentMapWithCourse(StudentDto studentDto, Long courseId) {
        Student student = studentMapper.toEntity(studentDto);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + courseId));

        Student_Course result = new Student_Course();
        result.setStudent(student);
        result.setCourse(course);
        result.setStatus("1");

        result = studentCourseRepository.save(result);

        return studentCourseMapper.toDto(result);
    }

    public List<CourseDto> findByStudentId(Long studentId) {
        List<Student_Course> studentCourseList = studentCourseRepository.findByStudentId(studentId);
        List<Course> courseList = studentCourseList.stream()
                .map(contain -> {
                    if (contain.getStatus().equals("1")) {
                        return contain.getCourse();
                    }
                    return null;
                })
                .collect(Collectors.toList());
        List<CourseDto> courseDtoList = courseMapper.DTO_LIST(courseList);
        return courseDtoList;
    }

    @Transactional
    public ApiResponse<Student_CourseDto> changeStatus(Long studentId, Long courseId, String status) {
        ApiResponse<Student_CourseDto> apiResponse = new ApiResponse<>();

//        Student_Course studentCourse = studentCourseRepository.findByStudentAndCourse(studentId, courseId);
//        studentCourse.setStatus(status);
//        studentCourse = studentCourseRepository.save(studentCourse);
//
//        Student_CourseDto result = new Student_CourseDto();
//        result = studentCourseMapper.toDto(studentCourse);
//
//        apiResponse.setResult(result);
        apiResponse.setMessage("Status chuyển thành " + status);
        return apiResponse;

    }

}
