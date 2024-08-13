package com.example.taskmanagerstudent.service;

import com.example.taskmanagerstudent.dto.request.CourseDto;
import com.example.taskmanagerstudent.dto.request.StudentDto;
import com.example.taskmanagerstudent.dto.request.UpdateStudentCourseDto;
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
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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
    private final MessageSource messageSource;

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final StudentCourseRepository studentCourseRepository;

    private StudentMapper studentMapper = StudentMapper.INSTANCE;
    private CourseMapper courseMapper = CourseMapper.INSTANCE;
    private Locale locale = LocaleContextHolder.getLocale();

    /**
     * GET
     */
    public ApiResponse<Page<StudentDto>> searchStudentAndTitleCourse(String name, int number, int size) {
        ApiResponse<Page<StudentDto>> response = new ApiResponse<>();
        response.setMessage(messageSource.getMessage("error.operation", null, locale));

        Pageable pageable = PageRequest.of(number, size);
        Page<Object[]> results = studentRepository.searchStudentAndTitleCourses(name, pageable);

        List<StudentDto> studentDtos = results.getContent()
                .stream().map(result -> {
                    StudentDto studentDto = new StudentDto();
                    studentDto.setId((Long) result[0]);
                    studentDto.setName((String) result[1]);
                    studentDto.setEmail((String) result[2]);
                    studentDto.setStatus((String) result[3]);
                    studentDto.setCourseTitles((String) result[4]);

                    return studentDto;
                }).collect(Collectors.toList());

        Page<StudentDto> result = new PageImpl<>(studentDtos, pageable, results.getTotalElements());

        response.setResult(result);
        response.setMessage(messageSource.getMessage("success.operation", null, locale));
        return response;
    }


    public ApiResponse<Page<StudentDto>> searchGetByNative(String nameStudent, int number, int size, Locale locale) {
        ApiResponse<Page<StudentDto>> response = new ApiResponse<>();
        response.setMessage(messageSource.getMessage("error.operation", null, locale));

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
                CourseDto courseDto = CourseDto.builder()
                        .id(courseId)
                        .title(courseTitle)
                        .description(courseDescription)
                        .status(courseStatus)
                        .build();
                studentDto.getCourseDtos().add(courseDto);
            }
        }
        
        List<StudentDto> result = new ArrayList<>(studentMap.values());
        Page<StudentDto> results = new PageImpl<>(result, pageable, searchList.getTotalElements());

        response.setResult(results);
        response.setMessage(messageSource.getMessage("success.operation", null, locale));

        return response;
    }


    public ApiResponse<Page<StudentDto>> searchGetByJPQL(String nameStudent, int number, int size) {
        ApiResponse<Page<StudentDto>> response = new ApiResponse<>();
        response.setMessage(messageSource.getMessage("error.operation", null, locale));

        Pageable pageable = PageRequest.of(number, size);
        Page<Object[]> resultsSearch = studentRepository.searchStudent(nameStudent, pageable);

        Map<Long, StudentDto> studentDtoMap = new HashMap<>();

        for (Object[] result : resultsSearch.getContent()) {
            Student student = (Student) result[0];
            Course course = (Course) result[1];

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

            if (course != null) {
                CourseDto courseDto = CourseDto.builder()
                        .id(course.getId())
                        .title(course.getTitle())
                        .description(course.getDescription())
                        .status(course.getStatus())
                        .build();
                studentDto.getCourseDtos().add(courseDto);
            }
        }

        List<StudentDto> studentDtos = new ArrayList<>(studentDtoMap.values());

        Page<StudentDto> results = new PageImpl<>(studentDtos, pageable, resultsSearch.getTotalElements());
        response.setResult(results);
        response.setMessage(messageSource.getMessage("success.operation", null, locale));

        return response;
    }


    public ApiResponse<StudentDto> getOneJpql(Long studentId) {
        List<Object[]> results = studentRepository.getStudentById(studentId);

        if (results.isEmpty()) {
            throw new AppException(ErrorCode.STUDENT_NOT_FOUND);
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
        apiResponse.setMessage(messageSource.getMessage("success.operation", null, locale));
        return apiResponse;
    }

    public ApiResponse<StudentDto> getOneNative(Long studentId) {
        List<Object[]> results = studentRepository.getStudentAndCourses(studentId);

        if (results.isEmpty()) {
            throw new AppException(ErrorCode.STUDENT_NOT_FOUND);
        }

        Long id = (Long) results.get(0)[0];
        String studentName = (String) results.get(0)[1];
        String email = (String) results.get(0)[2];
        String status = (String) results.get(0)[3];

        StudentDto studentDto = new StudentDto();
        studentDto.setId(id);
        studentDto.setName(studentName);
        studentDto.setEmail(email);
        studentDto.setStatus(status);

        List<CourseDto> courseDtos = results.stream()
                .map(result -> {
                    Long courseId = (Long) result[4];
                    String courseTitle = (String) result[5];
                    String courseDescription = (String) result[6];
                    String courseStatus = (String) result[7];  // Lấy đúng cột cho courseStatus

                    if (courseId != null) {
                        CourseDto courseDto = new CourseDto();
                        courseDto.setId(courseId);
                        courseDto.setTitle(courseTitle);
                        courseDto.setDescription(courseDescription);
                        courseDto.setStatus(courseStatus);
                        return courseDto;
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        studentDto.setCourseDtos(courseDtos);

        ApiResponse<StudentDto> apiResponse = new ApiResponse<>();
        apiResponse.setResult(studentDto);
        apiResponse.setMessage(messageSource.getMessage("success.operation", null, locale));

        return apiResponse;
    }


    /**
     * PUT
     */
    @Transactional
    public ApiResponse<StudentDto> update(UpdateStudentCourseDto inpUpdate) {
        Student student = studentRepository.findById(inpUpdate.getStudentId())
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));

        student.setName(inpUpdate.getName());
        student.setEmail(inpUpdate.getEmail());
        student.setStatus(inpUpdate.getStatus());
        studentRepository.save(student);

        // Tạo trực tiếp khóa học mới
        List<Course> courses = courseMapper.COURSES_LIST(inpUpdate.getNewCourses());
        courses = courseRepository.saveAll(courses);
        // Lấy ra id các khóa học mới
        Set<Long> newCreatedCourseIds = courses.stream()
                .map(Course::getId)
                .collect(Collectors.toSet());

        // Lấy ra StudentCourse, nghĩa là lấy ra các quan hệ Student và course
        List<Student_Course> existingStudentCourses = studentCourseRepository.findByStudentId(student.getId());
        Map<Long, Student_Course> studentCourseMap = existingStudentCourses.stream()
                .collect(Collectors.toMap(sc -> sc.getCourse().getId(), sc -> sc));

        // Thêm các Course id update và mới create
        Set<Long> newCourseIds = new HashSet<>(inpUpdate.getCourseIds());
        newCourseIds.addAll(newCreatedCourseIds);

        // Lấy ra các id có trong student course, nhưng không có trong update để hủy Course
        Set<Long> idToCloseCourses = studentCourseMap.keySet().stream()
                .filter(id -> !newCourseIds.contains(id))
                .collect(Collectors.toSet());

        if (!idToCloseCourses.isEmpty()) {
            studentCourseRepository.changeStatusByStudentIdAndCourseIds(student.getId(), idToCloseCourses, "0");
        }

        // Lấy tất cả Course id để cập nhập trong StudentCourse
        courses = courseRepository.findAllById(newCourseIds);

        Set<Student_Course> newStudentCourses = courses.stream()
                .map(course -> {
                    Student_Course studentCourse = studentCourseMap.get(course.getId());
                    if (studentCourse == null) {
                        studentCourse = new Student_Course();
                    }
                    studentCourse.setStudent(student);
                    studentCourse.setCourse(course);
                    studentCourse.setStatus("1");
                    return studentCourse;
                })
                .collect(Collectors.toSet());


        studentCourseRepository.saveAll(newStudentCourses);

        StudentDto result = studentMapper.toDto(student);
        result.setCourseDtos(courseMapper.DTO_LIST(courses));

        ApiResponse<StudentDto> response = new ApiResponse<>();
        response.setResult(result);
        response.setMessage(messageSource.getMessage("success.update", null, locale));
        return response;
    }


    /**
     * POST
     */
    @Transactional
    public ApiResponse<StudentDto> create(StudentDto studentDto) {
        Student student = new Student();
        student.setName(studentDto.getName());
        student.setEmail(studentDto.getEmail());
        student.setStatus(studentDto.getStatus());
        student = studentRepository.save(student);

        Set<Student_Course> studentCourses = new HashSet<>();

        for (CourseDto courseDto : studentDto.getCourseDtos()) {
            Long courseId = courseDto.getId();

            Course course;

            if (courseId == null) {
                course = new Course();
                course.setTitle(courseDto.getTitle());
                course.setDescription(courseDto.getDescription());
                course.setStatus(courseDto.getStatus());
                course = courseRepository.save(course);

                courseDto.setId(course.getId());
            } else {
                course = courseRepository.findById(courseId)
                        .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

                course.setTitle(courseDto.getTitle());
                course.setDescription(courseDto.getDescription());
                courseRepository.save(course);
            }
            Student_Course studentCourse = new Student_Course();
            studentCourse.setStudent(student);
            studentCourse.setCourse(course);
            studentCourse.setStatus(courseDto.getStatus());
            studentCourses.add(studentCourse);
        }
        studentCourseRepository.saveAll(studentCourses);

        StudentDto resultDto = studentMapper.toDto(student);
        resultDto.setCourseDtos(studentDto.getCourseDtos());

        ApiResponse<StudentDto> response = new ApiResponse<>();
        response.setResult(resultDto);
        response.setMessage(messageSource.getMessage("success.create", null, locale));

        return response;
    }


    /**
     * DELETE
     */
    public ApiResponse<Boolean> deleteTempStudent(Long studentId, String status) {
        ApiResponse<Boolean> apiResponse = new ApiResponse<>();

        apiResponse.setResult(false);
        apiResponse.setMessage(messageSource.getMessage("error.operation", null, locale));

        if (!studentRepository.existsById(studentId)) {
            apiResponse.setMessage(messageSource.getMessage("error.notFound", null, locale));
            apiResponse.setResult(false);
            return apiResponse;
        }

        try {
            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));
            student.setId(studentId);
            student.setStatus(status);
            student = studentRepository.save(student);
            apiResponse.setResult(true);
            apiResponse.setMessage(messageSource.getMessage("success.operation", null, locale));
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
            apiResponse.setMessage(messageSource.getMessage("error.notFound", null, locale));
            apiResponse.setResult(false);
            return apiResponse;
        }

        studentRepository.deleteById(id);

        apiResponse.setMessage(messageSource.getMessage("success.operation", null, locale));
        apiResponse.setResult(true);
        return apiResponse;
    }
}
