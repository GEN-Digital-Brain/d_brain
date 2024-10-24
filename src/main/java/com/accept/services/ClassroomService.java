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

import com.accept.dto.ClassDTO;
import com.accept.entities.Class;
import com.accept.entities.Student;
import com.accept.repositories.ClassRepository;
import com.accept.repositories.StudentRepository;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;

@Service
@Validated
@Tag(name = "Class Service", description = "Service layer for managing classes")
public class ClassroomService {

	@Autowired
	private final StudentRepository studentRepository;
	private final ClassRepository classRepository;
	private final ModelMapper modelMapper;

	public ClassroomService(StudentRepository studentRepository, ClassRepository classRepository, ModelMapper modelMapper) {
		this.studentRepository = studentRepository;
		this.classRepository = classRepository;
		this.modelMapper = modelMapper;
	}

	@Transactional(readOnly = true)
	public List<ClassDTO> getAllClasses() {
		List<Class> classes = classRepository.findAll();

		if (classes.isEmpty()) {
			throw new EntityNotFoundException("No classes found.");
		}
		return classes.stream().map(classEntity -> modelMapper.map(classEntity, ClassDTO.class))
				.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public ClassDTO getClassById(UUID classId) {
		Class classEntity = classRepository.findById(classId)
				.orElseThrow(() -> new IllegalArgumentException("Class not found: " + classId));
		return modelMapper.map(classEntity, ClassDTO.class);
	}

	@Transactional
	public ClassDTO createClass(ClassDTO classDTO) {
		validateRules(classDTO);
		Class classEntity = modelMapper.map(classDTO, Class.class);
		classEntity.onCreate();
		Class savedClass = classRepository.save(classEntity);
		return modelMapper.map(savedClass, ClassDTO.class);
	}

	@Transactional
	public ClassDTO updateClass(UUID classId, ClassDTO classDTO) {
		Class classEntity = classRepository.findById(classId)
				.orElseThrow(() -> new IllegalArgumentException("Class not found: " + classId));
		validateRules(classDTO);
		Student student = studentRepository.findById(classDTO.getStudent()).orElseThrow(
				() -> new IllegalArgumentException("Student not found with id: " + classDTO.getStudent()));

		classEntity.setClassName(classDTO.getName());
		classEntity.setInstructor(classDTO.getInstructor());
		classEntity.setStudent(student);
		classEntity.onUpdate();

		Class savedClass = classRepository.save(classEntity);
		return modelMapper.map(savedClass, ClassDTO.class);
	}

	@Transactional
	public void deleteClass(UUID classId) {
		Optional<Class> classOptional = classRepository.findById(classId);

		if (classOptional.isPresent()) {
			classRepository.delete(classOptional.get());
		} else {
			throw new EntityNotFoundException("Class not found with id: " + classId);
		}
	}

	private void validateRules(ClassDTO classDTO) {
		Optional<Class> existingClassByClassName = classRepository.findByClassName(classDTO.getName());

		if (existingClassByClassName.isPresent()) {
			throw new IllegalArgumentException("Class with the same name already exists.");
		}
		if (classDTO.getName().length() < 3) {
			throw new IllegalArgumentException("Class name must be at least 3 characters long.");
		}
	}

}