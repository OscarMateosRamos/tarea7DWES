package com.oscar.vivero;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class GestiondePersonasController {
	@GetMapping({ "/MenuAdmin", "GestiondePersonas" })
	public String GestiondePlantas() {
		return "GestiondePersonas";

	}

	@GetMapping("/GestionPersonasMenuAdmin")  
	public String mostrarMenuAdmin() {
	    return "MenuAdmin";
	}
}
