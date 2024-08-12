package com.example.taskmanagerstudent.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
    private Long id;

    @NotBlank(message = "Giá trị không thể rỗng")
    @Size(max = 50, min = 3, message = "Tên không thể nằm ngoài khoảng [3,50] từ")
    private String name;

    @Email(message = "Email không đúng định dạng")
    private String email;

    private String courses;

    private CourseDto courseDto;

    private List<@Valid CourseDto> courseDtos;
}
