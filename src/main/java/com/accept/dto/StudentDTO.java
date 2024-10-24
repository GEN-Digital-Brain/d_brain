package com.accept.dto;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(description = "Data Transfer Object for a Student")
@Tag(name = "Students DTO", description = "Data Transfer Object for a Student")
public @Data class StudentDTO {

	@Schema(description = "Unique identifier of the student", example = "d1f0c5f1-e9e8-473d-96df-1f5a5c98f35f")
	private UUID id;

	@NotBlank(message = "Full name is required")
	@Schema(description = "Full name of the student", example = "Levi Livinston")
	@Size(min = 3, message = "Minimum 3 characters")
	private String name;

	@NotBlank(message = "Email is required")
	@Schema(description = "E-mail of the student", example = "levi@gmail.com")
	@Size(max = 50, message = "Email cannot exceed 50 characters.")
	private String email;

	@NotNull(message = "Age is required")
	@Schema(description = "Age of the student", example = "20")
	private Integer age;

	@NotNull(message = "First semester grade is required")
	@Schema(description = "First semester grade of the student", example = "8.5")
	private Double firstSemesterGrade;

	@NotNull(message = "Second semester grade is required")
	@Schema(description = "Second semester grade of the student", example = "9.0")
	private Double secondSemesterGrade;

}