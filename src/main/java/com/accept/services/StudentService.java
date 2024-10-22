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
	public StudentDTO getStudentById(UUID studentId) {
		Student student = studentRepository.findById(studentId)
				.orElseThrow(() -> new IllegalArgumentException("Student not found"));
		return modelMapper.map(student, StudentDTO.class);
	}

	@Transactional
	public StudentDTO createStudent(StudentDTO studentDTO) {
		validateStudent(studentDTO);
		Student student = modelMapper.map(studentDTO, Student.class);
		student.onCreate();
		Student savedStudent = studentRepository.save(student);
		return modelMapper.map(savedStudent, StudentDTO.class);
	}

	@Transactional
	public StudentDTO updateStudent(UUID studentId, StudentDTO studentDTO) {
		Student student = studentRepository.findById(studentId)
				.orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));
		student.setFullName(studentDTO.getFullName());
		student.setAge(studentDTO.getAge());
		student.setTeacherName(studentDTO.getTeacherName());
		student.setRoomNumber(studentDTO.getRoomNumber());
		student.setFirstSemesterGrade(studentDTO.getFirstSemesterGrade());
		student.setSecondSemesterGrade(studentDTO.getSecondSemesterGrade());
		student.onUpdate();
		Student savedStudent = studentRepository.save(student);
		return modelMapper.map(savedStudent, StudentDTO.class);
	}

	@Transactional
	public void deleteStudent(UUID studentId) {
		Optional<Student> studentOptional = studentRepository.findById(studentId);
		if (studentOptional.isPresent()) {
			studentRepository.delete(studentOptional.get());
		} else {
			throw new EntityNotFoundException("Student not found with id: " + studentId);
		}
	}

	private void validateStudent(StudentDTO studentDTO) {
		Optional<Student> existingStudent = studentRepository.findByFullName(studentDTO.getFullName());
		if (existingStudent.isPresent()) {
			throw new IllegalArgumentException("Student with the same name already exists");
		}
	}

}