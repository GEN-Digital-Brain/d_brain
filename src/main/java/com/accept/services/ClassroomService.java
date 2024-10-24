package com.accept.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.accept.dto.ClassroomDTO;
import com.accept.entities.Classroom;
import com.accept.entities.Student;
import com.accept.repositories.ClassroomRepository;
import com.accept.repositories.StudentRepository;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@Service
@Validated
@Tag(name = "Classes Service", description = "Service layer for managing classes")
public class ClassroomService {

	@Autowired
	private final StudentRepository studentRepository;
	private final ClassroomRepository classroomRepository;
	private final ModelMapper modelMapper;

	public ClassroomService(StudentRepository studentRepository, ClassroomRepository classroomRepository,
			ModelMapper modelMapper) {
		this.studentRepository = studentRepository;
		this.classroomRepository = classroomRepository;
		this.modelMapper = modelMapper;
	}

	@Transactional(readOnly = true)
	public List<ClassroomDTO> getAll() {
		List<Classroom> classes = classroomRepository.findAll();

		if (classes.isEmpty()) {
			throw new EntityNotFoundException("No classes found.");
		}
		return classes.stream().map(classroom -> modelMapper.map(classroom, ClassroomDTO.class))
				.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public ClassroomDTO getById(UUID id) {
		Classroom classroom = classroomRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Class not found: " + id));
		return modelMapper.map(classroom, ClassroomDTO.class);
	}

	@Transactional
	public ClassroomDTO create(@Valid ClassroomDTO classroomDTO) {
		Classroom classroom = modelMapper.map(classroomDTO, Classroom.class);
		classroom.onCreate();
		return modelMapper.map(classroomRepository.save(classroom), ClassroomDTO.class);
	}

	@Transactional
	public ClassroomDTO update(UUID id, @Valid ClassroomDTO classroomDTO) {
		Classroom classroom = classroomRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Class not found: " + id));

//		Student student = studentRepository.findById(classroomDTO.getId())
//				.orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + classroomDTO.getId()));

		modelMapper.map(classroomDTO, classroom);
//		classroom.setStudent(student);
		classroom.onUpdate();
		return modelMapper.map(classroomRepository.save(classroom), ClassroomDTO.class);
	}

	@Transactional
	public void delete(UUID id) {
		classroomRepository.findById(id).ifPresentOrElse(classroomRepository::delete, () -> {
			throw new EntityNotFoundException("Class not found with id: " + id);
		});
	}

}