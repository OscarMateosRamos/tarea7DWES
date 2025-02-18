package com.oscar.vivero;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GestiondeEjemplaresController {
	@GetMapping({ "/MenuAdmin", "GestiondeEjemplares" })
	public String GestiondeEjemplares() {
		return "GestiondeEjemplares";

	}

	@GetMapping("/GestionEjemplaresMenuAdmin")
	public String mostrarMenuAdminEjemplares() {
		return "/MenuAdmin";
	}
}
