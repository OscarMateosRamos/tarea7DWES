package com.oscar.vivero;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class GestiondePlantasController {

	@GetMapping({ "GestiondePlantas" })
	public String GestiondePlantas() {

		return "/admin/GestiondePlantas";

	}

	@GetMapping("/GestionPlantasMenuAdmin")  
	public String mostrarMenuAdmin() {
	    return "/admin/MenuAdmin";
	}
}
