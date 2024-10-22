package com.accept;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
	@Value("${server.port}")
	private String serverPort;

	@EventListener(ApplicationReadyEvent.class)
	public void printSwaggerUrl() {
		System.out.println("\nApplication: http://localhost:" + serverPort);
		System.out.println("Documentation: http://localhost:" + serverPort + "/swagger-ui/index.html");
	}
	
}