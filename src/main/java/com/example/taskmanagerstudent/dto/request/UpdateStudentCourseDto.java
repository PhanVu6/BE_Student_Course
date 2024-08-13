package com.example.taskmanagerstudent.dto.request;

import com.example.taskmanagerstudent.entity.Student;
import com.example.taskmanagerstudent.repository.common.ExistsInDatabase;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateStudentCourseDto {
    @NotNull(message = "Giá trị không thể null")
    @ExistsInDatabase(entity = Student.class, message = "error.notFound")
    private Long studentId;

    @NotBlank(message = "error.notBlank")
    @Size(max = 50, min = 2, message = "error.invalidInput")
    private String name;
    @Email(message = "error.invalidInput")
    private String email;
    @Pattern(regexp = "1|0", message = "error.statusInput")
    private String status;
    private Set<Long> courseIds;
    private List<@Valid CourseDto> newCourses;
}
