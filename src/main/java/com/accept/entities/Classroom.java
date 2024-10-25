package com.accept.entities;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "classes")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)  // Explicitamente inclui os campos necessários
@ToString(onlyExplicitlyIncluded = true)  // Inclui apenas os campos relevantes no toString
@Schema(description = "Entity representing a classroom")
public class Classroom {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", columnDefinition = "BINARY(16)")
	@EqualsAndHashCode.Include  // Incluído para comparação de igualdade
	@ToString.Include  // Incluído na saída de toString
	@Schema(description = "Unique identifier of the class", example = "b2f8d5e7-4546-4a39-bad4-4e8b78537b9b")
	private UUID id;

	@NotBlank(message = "Classroom name is required")
	@Column(name = "name", columnDefinition = "VARCHAR(255) NOT NULL")
	@ToString.Include  // Incluído na saída de toString
	@Schema(description = "Name of the class", example = "Math 101")
	@Size(min = 3, message = "Minimum 3 characters")
	private String name;

	@NotBlank(message = "Instructor is required")
	@Column(name = "instructor", columnDefinition = "VARCHAR(255) NOT NULL")
	@ToString.Include  // Incluído na saída de toString
	@Schema(description = "Name of the class instructor", example = "John Doe")
	private String instructor;

	@OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@ToString.Exclude  // Excluído para evitar recursão e sobrecarga de saída
	@EqualsAndHashCode.Exclude  // Excluído da comparação de igualdade
	@Schema(description = "Students enrolled in the class")
	private List<Student> students;

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
