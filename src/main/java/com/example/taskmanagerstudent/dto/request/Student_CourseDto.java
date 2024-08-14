package com.example.taskmanagerstudent.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Student_CourseDto {

    private Long id;

    @Valid
    private StudentDto studentDto;

    @Valid
    private CourseDto courseDto;

    @NotNull(message = "error.notBlank")
    @Pattern(regexp = "0|1", message = "error.statusInput")
    private String status;

}
