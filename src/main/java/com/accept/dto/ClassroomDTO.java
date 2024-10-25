package com.accept.dto;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(description = "Data Transfer Object for a Classroom")
@Tag(name = "Classes DTO", description = "Data Transfer Object for a Classroom")
public @Data class ClassroomDTO {

	@Schema(description = "Unique identifier of the classroom", example = "b2f8d5e7-4546-4a39-bad4-4e8b78537b9b")
	private UUID id;

	@NotBlank(message = "Classroom name is required")
	@Schema(description = "Name of the classroom", example = "Math 101")
	@Size(min = 3, message = "Minimum 3 characters")
	private String name;

	@NotBlank(message = "Instructor name is required")
	@Schema(description = "Instructor of the classroom", example = "John Doe")
	private String instructor;

	@NotNull(message = "Student is required")
	@Schema(description = "Student associated with the classroom")
	private UUID studentId;

}