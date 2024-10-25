package com.accept.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accept.dto.StudentDTO;
import com.accept.services.StudentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Validated
@RestController
@RequestMapping("students")
@Tag(name = "Students Controller", description = "REST API for managing students")
public class StudentController {

	@Autowired
	private final StudentService studentService;

	public StudentController(StudentService studentService) {
		this.studentService = studentService;
	}

	@GetMapping
	@Operation(summary = "Get all Students", description = "Retrieve a list of all students")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentDTO.class))),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public ResponseEntity<List<StudentDTO>> getAll() {
		List<StudentDTO> students = studentService.getAll();
		return ResponseEntity.ok(students);
	}

	@GetMapping("{id}")
	@Operation(summary = "Get Student by ID", description = "Retrieve a specific student by their ID")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentDTO.class))),
			@ApiResponse(responseCode = "404", description = "Student not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public ResponseEntity<StudentDTO> getById(@PathVariable UUID id) {
		StudentDTO studentDTO = studentService.getById(id);
		return ResponseEntity.ok(studentDTO);
	}

	@PostMapping
	@Operation(summary = "Create a Student", description = "Creates a new student with the provided details")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Student created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentDTO.class))),
			@ApiResponse(responseCode = "400", description = "Bad request"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public ResponseEntity<StudentDTO> create(@Valid @RequestBody StudentDTO studentDTO) {
		StudentDTO createdStudent = studentService.create(studentDTO);
		return new ResponseEntity<>(createdStudent, HttpStatus.CREATED);
	}

	@PutMapping("{id}")
	@Operation(summary = "Update a Student", description = "Update the details of an existing student")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Student updated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StudentDTO.class))),
			@ApiResponse(responseCode = "404", description = "Student not found"),
			@ApiResponse(responseCode = "400", description = "Bad request"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public ResponseEntity<StudentDTO> update(@PathVariable UUID id, @Valid @RequestBody StudentDTO studentDTO) {
		StudentDTO updatedStudent = studentService.update(id, studentDTO);
		return ResponseEntity.ok(updatedStudent);
	}

	@DeleteMapping("{id}")
	@Operation(summary = "Delete a Student", description = "Deletes a specific student by their ID")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Student deleted successfully"),
			@ApiResponse(responseCode = "404", description = "Student not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	public ResponseEntity<String> delete(@PathVariable UUID id) {
		String message = "Student with ID " + id + " deleted successfully.";
		studentService.delete(id);
		return ResponseEntity.status(HttpStatus.OK).body(message);
	}

}