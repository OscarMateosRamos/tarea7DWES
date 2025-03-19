package com.oscar.vivero;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class Tarea7DwesOscarApplication {

	public static void main(String[] args) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

		String password = "admin";
		String hashedPassword = passwordEncoder.encode(password);
		System.out.println("Contraseña encriptada: " + hashedPassword);

		SpringApplication.run(Tarea7DwesOscarApplication.class, args);
	}

}
