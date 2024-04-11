package com.example.simpletaskmanagementsystem;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
		info = @Info(title = "Task manager API",
				description = "Simple task management system API", version = "v1.01")
)
@SpringBootApplication
public class SimpleTaskManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleTaskManagementSystemApplication.class, args);
	}

}
