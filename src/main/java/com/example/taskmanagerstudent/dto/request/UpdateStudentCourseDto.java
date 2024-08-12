package com.example.taskmanagerstudent.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateStudentCourseDto {
    @NotBlank(message = "Giá trị không thể rỗng")
    private Long id;
    @Pattern(regexp = "0|1", message = "Status phải là 0 hoặc 1")
    private String status;
}
