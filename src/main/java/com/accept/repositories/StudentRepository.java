package com.accept.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.accept.entities.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {
	
    Optional<Student> findByFullName(String fullName);
    
}