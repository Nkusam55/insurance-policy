package com.ipms.policy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI insuranceManagementOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("Insurance Policy Management System API")
						.description("API documentation for managing insurance policies and users.")
						.version("1.0"))
				.addTagsItem(new Tag().name("Welcome").description("APIs for testing for access"))
				.addTagsItem(new Tag().name("Users").description("APIs for managing user register and login"))
				.addTagsItem(new Tag().name("Policies").description("APIs for managing insurance policies like create, retrieve, update, delete"))
				.addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
				.components(new Components()
						.addSecuritySchemes("Bearer Authentication", new SecurityScheme()
								.name("Bearer Authentication")
								.type(SecurityScheme.Type.HTTP)
								.scheme("bearer")
								.bearerFormat("JWT")));
	}
}
