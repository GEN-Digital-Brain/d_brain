package com.accept.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.accept.entities.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {

	Optional<Student> findByName(String name);

	Optional<Student> findByEmail(String email);
	// Optional<Student> findByTuramaId(Long TurmaId);

}