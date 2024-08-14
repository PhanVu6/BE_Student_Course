package com.example.taskmanagerstudent.controller;

import com.example.taskmanagerstudent.dto.request.CourseDto;
import com.example.taskmanagerstudent.dto.request.Student_CourseDto;
import com.example.taskmanagerstudent.dto.response.ApiResponse;
import com.example.taskmanagerstudent.service.StudentCourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("student-course")
public class StudentCourseController {
    @Autowired
    private StudentCourseService studentCourseService;

    @PostMapping
    public ResponseEntity<Student_CourseDto> create(@RequestBody @Valid Student_CourseDto studentCourseDto) {
        Student_CourseDto result = studentCourseService.create(studentCourseDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<Student_CourseDto> update(@RequestParam("studentId") Long studentId,
                                                    @RequestParam("courseId") Long courseId) {
        Student_CourseDto result = studentCourseService.update(studentId, courseId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public List<CourseDto> findByStudent(@PathVariable("id") Long studentId) {
        return studentCourseService.findByStudentId(studentId);
    }

    @PutMapping("{studentId}/{courseId}")
    public ApiResponse<Student_CourseDto> changeStatus(@PathVariable("studentId") Long studentId,
                                                       @PathVariable("courseId") Long courseId,
                                                       @RequestParam("status") String status) {
        return studentCourseService.changeStatus(studentId, courseId, status);
    }
}
