package com.example.taskmanagerstudent.controller;

import com.example.taskmanagerstudent.dto.request.CourseDto;
import com.example.taskmanagerstudent.dto.response.ApiResponse;
import com.example.taskmanagerstudent.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "course")
@CrossOrigin(origins = "http://localhost:5173")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @GetMapping
    public ApiResponse<Page<CourseDto>> getAll(@RequestParam(value = "number", required = false, defaultValue = "0") int number,
                                               @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return courseService.getAll(number, size);
    }

    @GetMapping("get-course/{id}")
    public ApiResponse<CourseDto> getOne(@PathVariable("id") Long id) {
        return courseService.getOne(id);
    }

    @PutMapping("{id}")
    public ApiResponse<CourseDto> update(@RequestBody @Valid CourseDto courseDto, @PathVariable("id") Long id) {
        return courseService.update(courseDto, id);
    }

    @PutMapping("update-status/{id}")
    public ApiResponse<Boolean> updateCoursesByStudent(@RequestParam(value = "status", required = false, defaultValue = "1") String status,
                                                       @PathVariable("id") Long courseId) {
        return courseService.deleteTempCourse(courseId, status);
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
