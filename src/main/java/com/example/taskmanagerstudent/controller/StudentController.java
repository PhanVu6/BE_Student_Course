package com.example.taskmanagerstudent.controller;

import com.example.taskmanagerstudent.dto.request.StudentDto;
import com.example.taskmanagerstudent.dto.request.UpdateStudentCourseDto;
import com.example.taskmanagerstudent.dto.response.ApiResponse;
import com.example.taskmanagerstudent.repository.StudentRepository;
import com.example.taskmanagerstudent.service.StudentService;
import io.micrometer.common.lang.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("student")
//@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
@Validated
public class StudentController {
    private final StudentService studentService;
    private final StudentRepository studentRepository;

    /**
     * GET
     */
    @GetMapping("search")
    public ApiResponse<Page<StudentDto>> searchStudentAndTitles(@RequestParam(value = "name", required = false)
                                                                @Nullable
                                                                @Size(min = 2, max = 50, message = "error.invalidInput")
                                                                String nameStudent,
                                                                @RequestParam(value = "number", required = false, defaultValue = "0") int number,
                                                                @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(number, size);
        ApiResponse<Page<StudentDto>> result = studentService.searchStudentAndTitleCourse(nameStudent, pageable);
        return result;
    }

    @GetMapping("search-native")
    public ApiResponse<Page<StudentDto>> searchGetByNative(@RequestParam(value = "name", required = false)
                                                           @Nullable
                                                           @Size(min = 2, max = 50, message = "error.invalidInput")
                                                           String nameStudent,
                                                           @RequestParam(value = "number", required = false, defaultValue = "0") int number,
                                                           @RequestParam(value = "size", required = false, defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(number, size);
        ApiResponse<Page<StudentDto>> result = studentService.searchGetByNative(nameStudent, pageable);
        return result;
    }

    @GetMapping("search-jpql")
    public ApiResponse<Page<StudentDto>> searchGetByJPQL(@RequestParam(value = "name", required = false)
                                                         @Nullable
                                                         @Size(min = 2, max = 50, message = "error.invalidInput")
                                                         String nameStudent,
                                                         @RequestParam(value = "number", required = false, defaultValue = "0") int number,
                                                         @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        ApiResponse<Page<StudentDto>> result = studentService.searchGetByJPQL(nameStudent, number, size);
        return result;
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
    @PutMapping("put")
    public ApiResponse<StudentDto> update(@RequestBody @Valid UpdateStudentCourseDto inpUpdate) {
        return studentService.update(inpUpdate);
    }

    @PutMapping("delete-temp/{studentId}")
    public ApiResponse<Boolean> deleteTempStudent(@PathVariable("studentId") Long studentId,
                                                  @RequestParam("status")
                                                  @Pattern(regexp = "0|1", message = "error.statusInput")
                                                  String status) {
        return studentService.deleteTempStudent(studentId, status);
    }

    /**
     * POST
     */

    @PostMapping("save-many-course")
    public ApiResponse<StudentDto> create(@RequestBody @Valid StudentDto studentDto) {
        return studentService.create(studentDto);
    }

    /**
     * DELETE
     */
    @DeleteMapping("{id}")
    public ApiResponse<Boolean> delete(@PathVariable("id") Long id) {
        return studentService.delete(id);
    }
}
