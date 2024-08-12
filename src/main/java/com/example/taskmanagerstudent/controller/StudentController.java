package com.example.taskmanagerstudent.controller;

import com.example.taskmanagerstudent.dto.request.StudentDto;
import com.example.taskmanagerstudent.dto.response.ApiResponse;
import com.example.taskmanagerstudent.repository.StudentRepository;
import com.example.taskmanagerstudent.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.Locale;

@RestController
@RequestMapping("student")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;
    private final StudentRepository studentRepository;

    /**
     * GET
     */
    @GetMapping("search-native")
    public ApiResponse<Page<StudentDto>> searchGetByQuery(@RequestParam(value = "name", required = false) String nameStudent,
                                                          @RequestParam(value = "number", required = false, defaultValue = "0") int number,
                                                          @RequestParam(value = "size", required = false, defaultValue = "10") int size,
                                                          WebRequest request) {
        Locale locale = request.getHeader("Accept-Language") != null ?
                Locale.forLanguageTag(request.getHeader("Accept-Language")) : Locale.getDefault();
        ApiResponse<Page<StudentDto>> result = studentService.searchGetByNative(nameStudent, number, size, locale);
        return result;
    }

    @GetMapping("search-jpql")
    public ResponseEntity<Page<StudentDto>> searchGetByJPQL(@RequestParam(value = "name", required = false) String nameStudent,
                                                            @RequestParam(value = "number", required = false, defaultValue = "0") int number,
                                                            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        Page<StudentDto> result = studentService.searchGetByJPQL(nameStudent, number, size);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("get-student-jpql/{id}")
    public ApiResponse<StudentDto> getOne(@PathVariable("id") Long id) {
        return studentService.getOneJpql(id);
    }

    @GetMapping("get-student-native/{id}")
    public ApiResponse<StudentDto> getOneNative(@PathVariable("id") Long id) {
        return studentService.getOneNative(id);
    }

    /**
     * PUT
     */
    @PutMapping("{id}")
    public ApiResponse<StudentDto> update(@RequestBody @Valid StudentDto studentDto, @PathVariable("id") Long id) {
        return studentService.update(studentDto, id);
    }

    @PutMapping("update-to-course/{studentId}")
    public ApiResponse<StudentDto> updateCoursesByStudent(@PathVariable("studentId") Long studentId, @RequestParam("courseId") Long courseId) {
        return studentService.updateCoursesByStudent(studentId, courseId);
    }

    @PutMapping("delete-temp/{studentId}/{courseId}")
    public ApiResponse<Boolean> deleteTemp(@PathVariable("studentId") Long studentId, @PathVariable("courseId") Long courseId, @RequestParam("status") String status) {
        return studentService.deleteTemp(studentId, courseId, status);
    }

    /**
     * POST
     */
    @PostMapping
    public ApiResponse<StudentDto> save(@RequestBody @Valid StudentDto studentDto) {
        return studentService.save(studentDto);
    }

    @PostMapping("create")
    public ApiResponse<StudentDto> create(@RequestBody StudentDto studentDto, @RequestParam("courseId") Long courseId) {
        return studentService.create(studentDto, courseId);
    }

    @PostMapping("save-many-course")
    public ApiResponse<StudentDto> createStudentWithCourses(@RequestBody StudentDto studentDto) {
        return studentService.createStudentWithCourses(studentDto);
    }

    /**
     * DELETE
     */
    @DeleteMapping("{id}")
    public ApiResponse<Boolean> delete(@PathVariable("id") Long id) {
        return studentService.delete(id);
    }
}
