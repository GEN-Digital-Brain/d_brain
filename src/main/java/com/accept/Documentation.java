package com.accept;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class Documentation {
	
	@Value("${spring.application.name}")
    private String projectName;

	@Bean
	public GroupedOpenApi publicApi() {
		return GroupedOpenApi.builder()
				.group("public-api")
				.packagesToScan("com.accept.controllers")
				.build();
	}

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("API - " + projectName)
						.description("API project for Generation Brazil 2024")
						.version("v1.0")
				.license(new License()
						.name("Apache License 2.0")
						.url("https://github.com/Fiigueiredo/accept-me/tree/main?tab=Apache-2.0-1-ov-file"))
				.contact(new Contact()
						.name(projectName)
						.url("https://meaceita.vercel.app/")));
	}

}