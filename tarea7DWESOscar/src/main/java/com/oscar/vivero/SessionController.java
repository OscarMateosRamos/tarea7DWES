package com.oscar.vivero;

import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

public class SessionController {

	@GetMapping("/crearSesion")
	public String crearSession(HttpSession session) {

		session.setAttribute("username", "JohnDoe");

		String IdSession = session.getId();
		return "Session created with ID: " + IdSession;
	}
}
