package com.oscar.vivero;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GestiondeEjemplaresController {
	@GetMapping({ "GestiondeEjemplares" })
	public String GestiondeEjemplares() {
		return "GestiondeEjemplares";

	}

	@GetMapping("/GestiondeEjemplaresMenuAdmin")
	public String mostrarMenuAdminEjemplares() {
		return "redirect:/MenuAdmin";
	}
}
