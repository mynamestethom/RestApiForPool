package com.example.RestApiPool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestApiPoolApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestApiPoolApplication.class, args);
		System.out.println("Приложение запущено! Проверьте базу данных по адресу:");
		System.out.println("1. H2 Console: http://localhost:8080/h2-console");
		System.out.println("2. Тест базы: http://localhost:8080/test");
		System.out.println("Настройки подключения к H2:");
		System.out.println("JDBC URL: jdbc:h2:mem:pool_db");
		System.out.println("User Name: sa");
		System.out.println("Password: (оставьте пустым)");
	}

}
