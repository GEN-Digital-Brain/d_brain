package com.accept.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "employees", uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
@EqualsAndHashCode(of = "employeeId")
@ToString
@Schema(description = "Entity representing an employee")
@Data
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "employee_id", columnDefinition = "BINARY(16)")
    @Schema(description = "Unique identifier of the employee", example = "d1f0c5f1-e9e8-473d-96df-1f5a5c98f35f")
    private UUID employeeId;

    @NotBlank(message = "Name is required")
    @Column(name = "employee_name", columnDefinition = "VARCHAR(255) NOT NULL")
    @Schema(description = "Full name of the employee", example = "John Doe")
    private String employeeName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Column(name = "employee_email", columnDefinition = "VARCHAR(255) NOT NULL")
    @Schema(description = "Email of the employee", example = "john.doe@example.com")
    private String employeeEmail;

    @NotBlank(message = "Password is required")
    @Column(name = "password", columnDefinition = "VARCHAR(255) NOT NULL")
    @Schema(description = "Password of the employee", example = "password123")
    private String password;

    @NotBlank(message = "Position is required")
    @Column(name = "position", columnDefinition = "VARCHAR(255) NOT NULL")
    @Schema(description = "Position of the employee", example = "Software Engineer")
    private String position;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false, updatable = false)
    @Schema(description = "Timestamp when the student record was created", example = "2024-10-01T12:00:00Z")
    private Instant createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false)
    @Schema(description = "Timestamp when the student record was last updated", example = "2025-02-09T12:00:00Z")
    private Instant updatedAt;

    @PrePersist
    public void onCreate() {
        createdAt = Instant.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = Instant.now();
    }
}