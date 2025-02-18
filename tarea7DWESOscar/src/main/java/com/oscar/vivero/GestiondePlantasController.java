package com.oscar.vivero;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class GestiondePlantasController {

	@GetMapping({ "/MenuAdmin", "GestiondePlantas" })
	public String GestiondePlantas() {

		return "GestiondePlantas";

	}

	@GetMapping("/gestionPlantasMenuAdmin")  
	public String mostrarMenuAdmin() {
	    return "MenuAdmin";
	}
}
