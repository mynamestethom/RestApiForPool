package com.example.RestApiPool;

import com.example.RestApiPool.model.Reservation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestApiPoolApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestApiPoolApplication.class, args);
		System.out.println("\n База Данных H2 Console: http://localhost:8080/h2-console");

	}

}
