package com.example.taskmanagerstudent.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("student-page")
    public String getStudentPage(Model model) {
        return "student";
    }

    @GetMapping("student-course-page")
    public String getStudentCoursePage(Model model) {
        return "studentcourse";
    }

    @GetMapping("course-page")
    public String getCoursePage(Model model) {
        return "course";
    }
}
