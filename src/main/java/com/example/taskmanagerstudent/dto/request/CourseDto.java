package com.example.taskmanagerstudent.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseDto {
    private Long id;

    @NotBlank(message = "Giá trị không thể rỗng")
    @Size(max = 50, message = "Tên không thể nằm ngoài khoảng [3,50] từ")
    private String title;

    @Size(max = 50, message = "Mô tả không được vượt quá 1000 ký tự")
    private String description;

    @Pattern(regexp = "0|1", message = "Status phải là 0 hoặc 1")
    private String status;

    private List<@Valid StudentDto> studentDtos;
}
