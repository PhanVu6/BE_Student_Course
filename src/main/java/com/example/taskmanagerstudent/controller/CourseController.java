package com.example.taskmanagerstudent.controller;

import com.example.taskmanagerstudent.dto.request.CourseDto;
import com.example.taskmanagerstudent.dto.request.StudentDto;
import com.example.taskmanagerstudent.dto.response.ApiResponse;
import com.example.taskmanagerstudent.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "course")
@CrossOrigin(origins = "http://localhost:5173")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @GetMapping
    public ApiResponse<List<CourseDto>> getAll() {
        return courseService.getAll();
    }

    @GetMapping("get-course/{id}")
    public ApiResponse<CourseDto> getOne(@PathVariable("id") Long id) {
        return courseService.getOne(id);
    }

    @PutMapping("{id}")
    public ApiResponse<CourseDto> update(@RequestBody @Valid CourseDto courseDto, @PathVariable("id") Long id) {
        return courseService.update(courseDto, id);
    }

    @PutMapping("update-to-student/{id}")
    public ApiResponse<CourseDto> updateCoursesByStudent(@RequestBody @Valid StudentDto studentDto, @PathVariable("id") Long courseId) {
        return courseService.updateStudentsByCourse(studentDto, courseId);
    }

    @PostMapping
    public ApiResponse<CourseDto> save(@RequestBody @Valid CourseDto courseDto) {
        return courseService.save(courseDto);
    }

    @DeleteMapping("{id}")
    public ApiResponse<Boolean> delete(@PathVariable("id") Long id) {
        return courseService.delete(id);
    }
}
