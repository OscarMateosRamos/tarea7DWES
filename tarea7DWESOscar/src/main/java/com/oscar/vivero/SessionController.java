package com.oscar.vivero;

import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

public class SessionController {

	@GetMapping("/crearSesion")
	public String crearSession(HttpSession session) {

		session.setAttribute("username", "JohnDoe");

		String IdSession = session.getId();
		return "Sesion Creada con  ID: " + IdSession;
	}

	@GetMapping("/cogerSesion")
	public String cogerSesion(HttpSession session) {

		String usuario = (String) session.getAttribute("usuario");

		if (usuario == null) {
			return "No session found!";
		}

		return "Sesion encontrada con el usuario: " + usuario;
	}

	@GetMapping("/sesioninvalida")
	public String sesionInvalida(HttpSession session) {

		session.invalidate();
		return "¡Sesion Invalida!";
	}
}
