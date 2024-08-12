package com.example.taskmanagerstudent.service;

import com.example.taskmanagerstudent.dto.request.CourseDto;
import com.example.taskmanagerstudent.dto.request.StudentDto;
import com.example.taskmanagerstudent.dto.request.Student_CourseDto;
import com.example.taskmanagerstudent.dto.response.ApiResponse;
import com.example.taskmanagerstudent.entity.Course;
import com.example.taskmanagerstudent.entity.Student;
import com.example.taskmanagerstudent.entity.Student_Course;
import com.example.taskmanagerstudent.mapper.CourseMapper;
import com.example.taskmanagerstudent.mapper.StudentMapper;
import com.example.taskmanagerstudent.repository.CourseRepository;
import com.example.taskmanagerstudent.repository.StudentCourseRepository;
import com.example.taskmanagerstudent.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private MessageSource messageSource;

    private final StudentRepository studentRepository;

    private final CourseRepository courseRepository;

    private final StudentCourseRepository studentCourseRepository;

    private final StudentCourseService studentCourseService;
    private StudentMapper studentMapper = StudentMapper.INSTANCE;
    private CourseMapper courseMapper = CourseMapper.INSTANCE;

    /**
     * GET
     */
    public ApiResponse<Page<StudentDto>> searchGetByNative(String nameStudent, int number, int size, Locale locale) {

        Pageable pageable = PageRequest.of(number, size);
        Page<Object[]> searchList = studentRepository.searchByStudentCourses(nameStudent, pageable);

        Map<Long, StudentDto> studentMap = new HashMap<>();
        for (Object[] row : searchList.getContent()) {
            Long studentId = (Long) row[0];
            String studentName = (String) row[1];
            String studentEmail = (String) row[2];

            Long courseId = row[3] != null ? (Long) row[3] : null;
            String courseTitle = courseId != null ? (String) row[4] : null;
            String courseDescription = courseId != null ? (String) row[5] : null;

            StudentDto studentDto = studentMap.get(studentId);
            if (studentDto == null) {
                studentDto = StudentDto.builder()
                        .id(studentId)
                        .name(studentName)
                        .email(studentEmail)
                        .courseDtos(new ArrayList<>())
                        .build();
                studentMap.put(studentId, studentDto);
            }

            if (courseId != null) {
                CourseDto courseDto = CourseDto.builder()
                        .id(courseId)
                        .title(courseTitle)
                        .description(courseDescription)
                        .build();
                studentDto.getCourseDtos().add(courseDto);
            }
        }

        List<StudentDto> result = new ArrayList<>(studentMap.values());
        Page<StudentDto> results = new PageImpl<>(result, pageable, searchList.getTotalElements());

        ApiResponse<Page<StudentDto>> response = new ApiResponse<>();
        response.setResult(results);
        response.setMessage(result != null ? "Lấy thành công các student"
                : messageSource.getMessage("success.get.all", null, locale));

        return response;
    }


    public Page<StudentDto> searchGetByJPQL(String nameStudent, int number, int size) {
        Pageable pageable = PageRequest.of(number, size);
        Page<Object[]> resultsSearch = studentRepository.searchStudent(nameStudent, pageable);

        Map<Long, StudentDto> studentDtoMap = new HashMap<>();

        for (Object[] result : resultsSearch.getContent()) {
            Student student = (Student) result[0];
            Course course = (Course) result[1];

            // Kiểm tra xem StudentDto đã được tạo hay chưa
            StudentDto studentDto = studentDtoMap.get(student.getId());

            if (studentDto == null) {
                studentDto = StudentDto.builder()
                        .id(student.getId())
                        .name(student.getName())
                        .email(student.getEmail())
                        .courseDtos(new ArrayList<>())
                        .build();
                studentDtoMap.put(student.getId(), studentDto);
            }

            //Nếu course không null, tạo CourseDto và thêm vào danh sách courseDtos của StudentDto
            if (course != null) {
                CourseDto courseDto = CourseDto.builder()
                        .id(course.getId())
                        .title(course.getTitle())
                        .description(course.getDescription())
                        .build();
                studentDto.getCourseDtos().add(courseDto);
            }
        }

        List<StudentDto> results = new ArrayList<>(studentDtoMap.values());

        return new PageImpl<>(results, pageable, resultsSearch.getTotalElements());
    }

    public ApiResponse<StudentDto> getOneJpql(Long studentId) {
        List<Object[]> results = studentRepository.getStudentById(studentId);

        if (results.isEmpty()) {
            throw new EntityNotFoundException("Student không tìm thấy với id = " + studentId);
        }

        Student student = (Student) results.get(0)[0];

        List<Course> courses = results.stream()
                .map(result -> (Course) result[1])
                .distinct()
                .collect(Collectors.toList());

        StudentDto result = studentMapper.toDto(student);
        result.setCourseDtos(courseMapper.DTO_LIST(courses));

        ApiResponse<StudentDto> apiResponse = new ApiResponse<>();
        apiResponse.setResult(result);
        apiResponse.setMessage(result != null ? "Thành công lấy student" : "Thất bại lấy student");
        return apiResponse;
    }

    public ApiResponse<StudentDto> getOneNative(Long studentId) {
        List<Object[]> results = studentRepository.getStudentAndCourses(studentId);

        if (results.isEmpty()) {
            throw new EntityNotFoundException("Student không tìm thấy với id = " + studentId);
        }

        Long id = (Long) results.get(0)[0];
        String studentName = (String) results.get(0)[1];
        String email = (String) results.get(0)[2];

        // Map student
        StudentDto studentDtos = new StudentDto();
        studentDtos.setId(id);
        studentDtos.setName(studentName);
        studentDtos.setEmail(email);

        // Map course
        List<CourseDto> courseDtos = results.stream()
                .map(result -> {
                    Long courseId = (Long) result[3];
                    String courseTitle = (String) result[4];
                    String courseDescription = (String) result[5];

                    CourseDto course = new CourseDto();
                    course.setId(courseId);
                    course.setTitle(courseTitle);
                    course.setDescription(courseDescription);
                    return course;
                })
                .collect(Collectors.toList());

        StudentDto result = (studentDtos);
        result.setCourseDtos(courseDtos);

        ApiResponse<StudentDto> apiResponse = new ApiResponse<>();
        apiResponse.setResult(result);
        apiResponse.setMessage(result != null ? "Thành công lấy student" : "Thất bại lấy student");
        return apiResponse;
    }


    /**
     * PUT
     */
    @Transactional
    public ApiResponse<StudentDto> update(StudentDto studentDto, Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy student id = " + id));

        student.setName(studentDto.getName());
        student.setEmail(studentDto.getEmail());


        Set<Student_Course> updatedStudentCourses = new HashSet<>();
        for (CourseDto courseDto : studentDto.getCourseDtos()) {
            Course course = courseRepository.findById(courseDto.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Không tồn tại khóa học" + courseDto.getId()));

            course.setTitle(courseDto.getTitle());
            course.setDescription(courseDto.getDescription());
            course = courseRepository.save(course);

            Student_Course studentCourse = studentCourseRepository
                    .findByStudentAndCourse(student.getId(), course.getId())
                    .orElseGet(() -> new Student_Course());

            studentCourse.setStudent(student);
            studentCourse.setCourse(course);
            studentCourse.setStatus("1");
            updatedStudentCourses.add(studentCourse);
        }

        student.getStudent_courses().clear();
        student.getStudent_courses().addAll(updatedStudentCourses);

        student = studentRepository.save(student);

        StudentDto result = studentMapper.toDto(student);
        result.setCourseDtos(studentDto.getCourseDtos());

        ApiResponse<StudentDto> response = new ApiResponse<>();
        response.setResult(result);
        response.setMessage("Cập nhật sinh viên thành công");
        return response;
    }

    /**
     * POST
     */
    @Transactional
    public ApiResponse<StudentDto> createStudentWithCourses(StudentDto studentDto) {
        Student student = new Student();
        student.setName(studentDto.getName());
        student.setEmail(studentDto.getEmail());

        Student savedStudent = studentRepository.save(student);

        for (CourseDto courseDto : studentDto.getCourseDtos()) {
            Course course = courseRepository.findById(courseDto.getId())
                    .orElseGet(() -> {
                        Course newCourse = new Course();
                        newCourse.setTitle(courseDto.getTitle());
                        newCourse.setDescription(courseDto.getDescription());
                        return courseRepository.save(newCourse);
                    });

            Student_Course studentCourse = new Student_Course();
            studentCourse.setStudent(savedStudent);
            studentCourse.setCourse(course);
            studentCourse.setStatus("1");
            studentCourseRepository.save(studentCourse);
        }

        studentDto.setId(savedStudent.getId());
        ApiResponse<StudentDto> response = new ApiResponse<>();
        response.setResult(studentDto);
        response.setMessage("Thêm sinh viên thành công");

        return response;
    }

    @Transactional
    public ApiResponse<StudentDto> save(StudentDto studentDto) {
        Student student = studentMapper.toEntity(studentDto);
        student = studentRepository.save(student);
        StudentDto result = studentMapper.toDto(student);

        ApiResponse<StudentDto> apiResponse = new ApiResponse<>();
        apiResponse.setResult(result);
        apiResponse.setMessage(result != null ? "Thành công tạo student" : "Thất bại tạo student");
        return apiResponse;
    }

    @Transactional
    public ApiResponse<StudentDto> create(StudentDto studentDto, Long courseId) {
        Student_CourseDto studentCourseDto = studentCourseService.createStudentMapWithCourse(studentDto, courseId);

        StudentDto result = studentCourseDto.getStudentDto();
        result.setCourseDto(studentCourseDto.getCourseDto());

        ApiResponse<StudentDto> apiResponse = new ApiResponse<>();
        apiResponse.setResult(result);
        apiResponse.setMessage(result != null ? "Tạo student thành công" : "Tạo student thất bại");
        return apiResponse;
    }


    /**
     * DELETE
     */
    public ApiResponse<Boolean> deleteTemp(Long studentId, Long courseId, String status) {
        ApiResponse<Boolean> apiResponse = new ApiResponse<>();
        apiResponse.setResult(false);
        apiResponse.setMessage("Chuyển đổi status thất bại");

        try {
            studentCourseService.changeStatus(studentId, courseId, status);
            apiResponse.setResult(true);
            apiResponse.setMessage("Chuyển đổi status thành công");
            return apiResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return apiResponse;
        }
    }

    @Transactional
    public ApiResponse<Boolean> delete(Long id) {
        ApiResponse<Boolean> apiResponse = new ApiResponse<>();
        if (!studentRepository.existsById(id)) {
            apiResponse.setMessage("Student không tồn tại");
            apiResponse.setResult(false);
            return apiResponse;
        }

        studentRepository.deleteById(id);

        apiResponse.setMessage("Student đã xóa");
        apiResponse.setResult(true);
        return apiResponse;
    }
}
