package com.accept.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Schema(description = "Data Transfer Object for an Employee")
@Tag(name = "Employee DTO", description = "Data Transfer Object for an Employee")
@Data
public class EmployeeDTO {

    @Schema(description = "Unique identifier of the employee", example = "d1f0c5f1-e9e8-473d-96df-1f5a5c98f35f")
    private UUID employeeId;

    @NotBlank(message = "Name is required")
    @Schema(description = "Full name of the employee", example = "John Doe")
    private String employeeName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Schema(description = "Email of the employee", example = "john.doe@example.com")
    private String employeeEmail;

    @NotBlank(message = "Password is required")
    @Schema(description = "Password of the employee", example = "password123")
    private String password;

    @NotBlank(message = "Position is required")
    @Schema(description = "Position of the employee", example = "Software Engineer")
    private String position;

}