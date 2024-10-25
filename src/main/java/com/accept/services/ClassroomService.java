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
		return classes.stream()
				.map(this::convertToDTO)  // Usa o método auxiliar para incluir studentIds
				.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public ClassroomDTO getById(UUID id) {
		Classroom classroom = classroomRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Class not found: " + id));
		return convertToDTO(classroom);  // Usa o método auxiliar
	}

	@Transactional
	public ClassroomDTO create(@Valid ClassroomDTO classroomDTO) {
		Classroom classroom = modelMapper.map(classroomDTO, Classroom.class);
		classroom.setStudents(getStudentsFromIds(classroomDTO.getStudentIds()));  // Associação de estudantes
		classroom.onCreate();
		return convertToDTO(classroomRepository.save(classroom));  // Usa o método auxiliar
	}

	@Transactional
	public ClassroomDTO update(UUID id, @Valid ClassroomDTO classroomDTO) {
		Classroom classroom = classroomRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Class not found: " + id));

		// Atualiza os dados da turma
		modelMapper.map(classroomDTO, classroom);
		classroom.setStudents(getStudentsFromIds(classroomDTO.getStudentIds()));  // Associação de estudantes
		classroom.onUpdate();

		return convertToDTO(classroomRepository.save(classroom));  // Usa o método auxiliar
	}

	@Transactional
	public void delete(UUID id) {
		classroomRepository.findById(id).ifPresentOrElse(classroomRepository::delete, () -> {
			throw new EntityNotFoundException("Class not found with id: " + id);
		});
	}

	// Método auxiliar para buscar e validar estudantes pelos seus IDs
	private List<Student> getStudentsFromIds(List<UUID> studentIds) {
		return studentIds.stream()
				.map(id -> studentRepository.findById(id)
						.orElseThrow(() -> new EntityNotFoundException("Student not found with id: " + id)))
				.collect(Collectors.toList());
	}

	// Método auxiliar para converter Classroom para ClassroomDTO, incluindo studentIds
	private ClassroomDTO convertToDTO(Classroom classroom) {
		ClassroomDTO dto = modelMapper.map(classroom, ClassroomDTO.class);
		dto.setStudentIds(classroom.getStudents().stream()
				.map(Student::getId)
				.collect(Collectors.toList()));
		return dto;
	}
}
