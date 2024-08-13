package com.example.taskmanagerstudent.dto.request;

import com.example.taskmanagerstudent.entity.Student;
import com.example.taskmanagerstudent.repository.common.ExistsInDatabase;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentDto {
    @ExistsInDatabase(entity = Student.class, message = "error.notFound")
    private Long id;

    @NotBlank(message = "error.notBlank")
    @Size(max = 50, min = 2, message = "error.invalidInput")
    private String name;

    @Email(message = "error.invalidInput")
    private String email;

    @Pattern(regexp = "0|1", message = "error.statusInput")
    private String status;

    private CourseDto courseDto;

    private String courseTitles;

    private List<@Valid CourseDto> courseDtos;
}
