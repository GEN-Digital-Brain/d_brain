package com.accept;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;

import com.accept.dto.ClassroomDTO;
import com.accept.dto.StudentDTO;
import com.accept.entities.Classroom;
import com.accept.entities.Student;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();

		modelMapper.addMappings(new PropertyMap<StudentDTO, Student>() {
			@Override
			protected void configure() {
				skip(destination.getId());
			}
		});

		modelMapper.addMappings(new PropertyMap<ClassroomDTO, Classroom>() {
			@Override
			protected void configure() {
				skip(destination.getId());
			}
		});

		return modelMapper;
	}

	@Value("${server.port}")
	private String serverPort;

	@EventListener(ApplicationReadyEvent.class)
	public void printSwaggerUrl() {
		System.out.println("\nApplication: http://localhost:" + serverPort);
		System.out.println("Documentation: http://localhost:" + serverPort + "/swagger-ui/index.html");
	}

}