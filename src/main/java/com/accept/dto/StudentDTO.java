package com.accept.dto;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "Data Transfer Object for a Student")
@Tag(name = "Student DTO", description = "Data Transfer Object for a Student")
public @Data class StudentDTO {

    @Schema(description = "Unique identifier of the student", example = "d1f0c5f1-e9e8-473d-96df-1f5a5c98f35f")
    private UUID studentId;

    @NotBlank(message = "Full name is required")
    @Schema(description = "Full name of the student", example = "Levi Livinston")
    private String fullName;

    @NotNull(message = "Age is required")
    @Schema(description = "Age of the student", example = "20")
    private Integer age;

    @NotBlank(message = "Teacher's name is required")
    @Schema(description = "Teacher's name of the student", example = "Gustavo Boaz")
    private String teacherName;

    @NotBlank(message = "Room number is required")
    @Schema(description = "Room number of the student", example = "A101")
    private String roomNumber;

    @NotNull(message = "First semester grade is required")
    @Schema(description = "First semester grade of the student", example = "8.5")
    private Double firstSemesterGrade;

    @NotNull(message = "Second semester grade is required")
    @Schema(description = "Second semester grade of the student", example = "9.0")
    private Double secondSemesterGrade;

}