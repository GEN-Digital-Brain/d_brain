package com.accept.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.accept.dto.StudentDTO;
import com.accept.entities.Student;
import com.accept.repositories.StudentRepository;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@Service
@Validated
@Tag(name = "Student Service", description = "Service layer for managing student")
public class StudentService {

	@Autowired
	private final StudentRepository studentRepository;
	private final ModelMapper modelMapper;

	public StudentService(StudentRepository studentRepository, ModelMapper modelMapper) {
		this.studentRepository = studentRepository;
		this.modelMapper = modelMapper;
	}

	@Transactional(readOnly = true)
	public List<StudentDTO> getAllStudents() {
		List<Student> students = studentRepository.findAll();

		if (students.isEmpty()) {
			throw new EntityNotFoundException("No students found.");
		}
		return students.stream().map(student -> modelMapper.map(student, StudentDTO.class))
				.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public StudentDTO getStudentById(UUID id) {
		Student student = studentRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Student not found"));
		return modelMapper.map(student, StudentDTO.class);
	}

	@Transactional
	public StudentDTO createStudent(@Valid StudentDTO studentDTO) {
		validateRules(studentDTO);
		Student student = modelMapper.map(studentDTO, Student.class);
		student.onCreate();
		return modelMapper.map(studentRepository.save(student), StudentDTO.class);
	}

	@Transactional
	public StudentDTO updateStudent(UUID id, @Valid StudentDTO studentDTO) {
		Student student = studentRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Student not found: " + id));
		validateRules(studentDTO);

		modelMapper.map(studentDTO, student);
		student.onUpdate();
		return modelMapper.map(studentRepository.save(student), StudentDTO.class);
	}

	@Transactional
	public void deleteStudent(UUID id) {
		studentRepository.findById(id).ifPresentOrElse(studentRepository::delete, () -> {
			throw new EntityNotFoundException("Student not found with id: " + id);
		});
	}

	private void validateRules(StudentDTO studentDTO) {
		Optional<Student> existingStudentByEmail = studentRepository.findByEmail(studentDTO.getEmail());
		if (existingStudentByEmail.isPresent()) {
			throw new IllegalArgumentException("Student with the same email already exists.");
		}
		if (!isValidEmail(studentDTO.getEmail())) {
			throw new IllegalArgumentException("Invalid email format. Please provide a valid email address.");
		}
		if (studentDTO.getEmail().length() > 50) {
			throw new IllegalArgumentException("The limit of characters allowed to e-mails here is 50.");
		}
	}

	private boolean isValidEmail(String email) {
		String emailRegex = "^[\\w.-]+@[\\w-]+\\.[a-zA-Z]{2,}(\\.[a-zA-Z]{2,})?$";
		return email.matches(emailRegex);
	}

}