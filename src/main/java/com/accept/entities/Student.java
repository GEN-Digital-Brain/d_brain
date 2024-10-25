package com.accept.entities;

import java.time.Instant;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "students", uniqueConstraints = { @UniqueConstraint(columnNames = "email") })
@EqualsAndHashCode(of = "id")
@ToString
@Schema(description = "Entity representing a student")
public @Data class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", columnDefinition = "BINARY(16)")
	@Schema(description = "Unique identifier of the student", example = "d1f0c5f1-e9e8-473d-96df-1f5a5c98f35f")
	private UUID id;

	@NotBlank(message = "Name is required")
	@Column(name = "name", columnDefinition = "VARCHAR(255) NOT NULL")
	@Schema(description = "Full name of the student", example = "Levi Livinston")
	@Size(min = 3, message = "Minimum 3 characters")
	private String name;

	@NotBlank(message = "E-mail is required")
	@Email(message = "E-mail should be valid.")
	@Size(max = 50, message = "Email cannot exceed 50 characters.")
	private String email;

	@NotNull(message = "Age is required")
	@Column(name = "age", nullable = false)
	@Schema(description = "Age of the student", example = "20")
	private Integer age;

	@NotNull(message = "First semester grade is required")
	@Column(name = "first_semester_grade", nullable = false)
	@Schema(description = "First semester grade of the student", example = "8.5")
	private Double firstSemesterGrade;

	@NotNull(message = "Second semester grade is required")
	@Column(name = "second_semester_grade", nullable = false)
	@Schema(description = "Second semester grade of the student", example = "9.0")
	private Double secondSemesterGrade;

	// Relacionamento ManyToOne com Classroom
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "classroom_id", nullable = false)
	@Schema(description = "Classroom to which the student belongs")
	private Classroom classroom;

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