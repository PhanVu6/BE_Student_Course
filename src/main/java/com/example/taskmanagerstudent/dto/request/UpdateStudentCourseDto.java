package com.example.taskmanagerstudent.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateStudentCourseDto {
    @NotNull(message = "Giá trị không thể null")
    private Long studentId;
    @NotBlank(message = "Giá trị không thể rỗng")
    @Size(max = 50, min = 3, message = "Tên không thể nằm ngoài khoảng [3,50] từ")
    private String name;
    @Email(message = "Email không đúng định dạng")
    private String email;
    @Pattern(regexp = "1|0", message = "Status phải là 1 hoặc 0")
    private String status;
    private Set<Long> courseIds;
}
