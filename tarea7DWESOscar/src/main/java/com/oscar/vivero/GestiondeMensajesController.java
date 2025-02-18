package com.oscar.vivero;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GestiondeMensajesController {
	@GetMapping({ "/MenuAdmin", "GestiondeMensajes" })
	public String GestiondePlantas() {
		return "GestiondeMensajes";

	}
	
	@GetMapping("/GestionMensajesMenuAdmin")  
	public String mostrarMenuAdmin() {
	    return "MenuAdmin";
	}
}
