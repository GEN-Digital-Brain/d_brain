package com.accept.entities;

import java.time.Instant;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "classes")
@EqualsAndHashCode(of = "id")
@ToString
@Schema(description = "Entity representing a classroom")
public @Data class Classroom {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", columnDefinition = "BINARY(16)")
	@Schema(description = "Unique identifier of the class", example = "b2f8d5e7-4546-4a39-bad4-4e8b78537b9b")
	private UUID id;

	@NotBlank(message = "Classroom name is required")
	@Column(name = "name", columnDefinition = "VARCHAR(255) NOT NULL")
	@Schema(description = "Name of the class", example = "Math 101")
	@Size(min = 3, message = "Minimum 3 characters")
	private String name;

	@NotBlank(message = "Instructor is required")
	@Column(name = "instructor", columnDefinition = "VARCHAR(255) NOT NULL")
	@Schema(description = "Name of the class instructor", example = "John Doe")
	private String instructor;

	@ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
	@JoinColumn(name = "student_id", nullable = false)
	@Schema(description = "Student enrolled in the class")
	private Student student;

	@Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false, updatable = false)
	@Schema(description = "Timestamp when the class record was created", example = "2024-10-01T12:00:00Z")
	private Instant createdAt;

	@Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false)
	@Schema(description = "Timestamp when the class record was last updated", example = "2025-02-09T12:00:00Z")
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