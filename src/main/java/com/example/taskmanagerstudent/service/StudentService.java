package com.example.taskmanagerstudent.service;

import com.example.taskmanagerstudent.dto.request.CourseDto;
import com.example.taskmanagerstudent.dto.request.StudentDto;
import com.example.taskmanagerstudent.dto.request.Student_CourseDto;
import com.example.taskmanagerstudent.dto.response.ApiResponse;
import com.example.taskmanagerstudent.entity.Course;
import com.example.taskmanagerstudent.entity.Student;
import com.example.taskmanagerstudent.entity.Student_Course;
import com.example.taskmanagerstudent.exception.AppException;
import com.example.taskmanagerstudent.exception.ErrorCode;
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
            String studentStatus = (String) row[3];

            Long courseId = (Long) row[4];
            String courseTitle = courseId != null ? (String) row[5] : null;
            String courseDescription = courseId != null ? (String) row[6] : null;
            String courseStatus = courseId != null ? (String) row[7] : null;

            StudentDto studentDto = studentMap.get(studentId);
            if (studentDto == null) {
                studentDto = StudentDto.builder()
                        .id(studentId)
                        .name(studentName)
                        .email(studentEmail)
                        .status(studentStatus)
                        .courseDtos(new ArrayList<>())
                        .build();
                studentMap.put(studentId, studentDto);
            }


            if (courseId != null) {
                Student_Course studentCourse = studentCourseRepository
                        .findByStudentAndCourse(studentId, courseId)
                        .orElseThrow(() -> new AppException(ErrorCode.STUDENT_EXISTS_COURSE));

                if (studentCourse.getStatus().equals("1")) {
                    CourseDto courseDto = CourseDto.builder()
                            .id(courseId)
                            .title(courseTitle)
                            .description(courseDescription)
                            .status(courseStatus)
                            .build();
                    studentDto.getCourseDtos().add(courseDto);
                }
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
                        .status(student.getStatus())
                        .courseDtos(new ArrayList<>())
                        .build();
                studentDtoMap.put(student.getId(), studentDto);
            }

            //Nếu course không null, tạo CourseDto và thêm vào danh sách courseDtos của StudentDto
            if (course != null) {
                Student_Course studentCourse = studentCourseRepository
                        .findByStudentAndCourse(student.getId(), course.getId())
                        .orElseThrow(() -> new AppException(ErrorCode.STUDENT_EXISTS_COURSE));

                if (studentCourse.getStatus().equals("1")) {
                    CourseDto courseDto = CourseDto.builder()
                            .id(course.getId())
                            .title(course.getTitle())
                            .description(course.getDescription())
                            .status(course.getStatus())
                            .build();
                    studentDto.getCourseDtos().add(courseDto);
                }
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
                .map(result -> {
                    Course course = (Course) result[1];
                    Student_Course studentCourse = studentCourseRepository
                            .findByStudentAndCourse(studentId, course.getId())
                            .orElseThrow(() -> new AppException(ErrorCode.STUDENT_EXISTS_COURSE));

                    if (studentCourse.getStatus().equals("1")) {
                        return course;
                    }
                    return null;
                })
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
        String status = (String) results.get(0)[3];

        // Map student
        StudentDto studentDtos = new StudentDto();
        studentDtos.setId(id);
        studentDtos.setName(studentName);
        studentDtos.setEmail(email);
        studentDtos.setStatus(status);

        // Map course
        List<CourseDto> courseDtos = results.stream()
                .map(result -> {
                    Long courseId = (Long) result[4];
                    String courseTitle = (String) result[5];
                    String courseDescription = (String) result[6];
                    String courseStatus = (String) result[6];

                    if (courseId != null) {
                        Student_Course studentCourse = studentCourseRepository
                                .findByStudentAndCourse(studentId, courseId)
                                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_EXISTS_COURSE));

                        if (studentCourse.getStatus().equals("1")) {
                            CourseDto course = new CourseDto();
                            course.setId(courseId);
                            course.setTitle(courseTitle);
                            course.setDescription(courseDescription);
                            course.setStatus(courseStatus);
                            return course;
                        }
                    }
                    return null;
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

        studentMapper.updateStudentByStudentDto(studentDto, student);
        student.setId(id);

        List<Long> courseIds = studentDto.getCourseDtos().stream()
                .map(CourseDto::getId)
                .collect(Collectors.toList());

        List<Course> courses = courseRepository.findAllById(courseIds);

        // Tạo danh sách Student_Course mới
        Set<Student_Course> updatedStudentCourses = new HashSet<>();

        for (Course course : courses) {
            CourseDto courseDto = studentDto.getCourseDtos().stream()
                    .filter(dto -> dto.getId().equals(course.getId()))
                    .findFirst()
                    .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

            Student_Course studentCourse = studentCourseRepository
                    .findByStudentAndCourse(student.getId(), course.getId())
                    .orElseGet(() -> new Student_Course());

            studentCourse.setStudent(student);
            studentCourse.setCourse(course);
            studentCourse.setStatus(courseDto.getStatus());
            updatedStudentCourses.add(studentCourse);
        }

        // Cập nhật quan hệ giữa Student và Courses
        student.setStudent_courses(updatedStudentCourses);
        student = studentRepository.save(student);

        // Chuyển đổi Student entity thành DTO để trả về
        StudentDto result = studentMapper.toDto(student);
        result.setCourseDtos(studentDto.getCourseDtos());

        // Trả về ApiResponse với thông báo thành công
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


        Set<Student_Course> updatedStudentCourses = new HashSet<>();
        for (CourseDto courseDto : studentDto.getCourseDtos()) {
            Course course = courseRepository.findById(courseDto.getId())
                    .orElseGet(() -> new Course());


            Student_Course studentCourse = studentCourseRepository
                    .findByStudentAndCourse(student.getId(), course.getId())
                    .orElseGet(() -> new Student_Course());

            studentCourse.setStudent(student);
            studentCourse.setCourse(course);
            studentCourse.setStatus(courseDto.getStatus());
            updatedStudentCourses.add(studentCourse);
        }

        student.setStudent_courses(updatedStudentCourses);
        student = studentRepository.save(student);

        StudentDto result = studentMapper.toDto(student);
        result.setCourseDtos(studentDto.getCourseDtos());

        ApiResponse<StudentDto> response = new ApiResponse<>();
        response.setResult(result);
        response.setMessage("Tạo sinh viên thành công");
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
