package com.accept.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.accept.entities.Classroom;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, UUID> {

}