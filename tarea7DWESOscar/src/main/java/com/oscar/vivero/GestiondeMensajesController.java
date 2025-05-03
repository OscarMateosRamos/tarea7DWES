package com.oscar.vivero;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class GestiondeMensajesController {
	@GetMapping({ "GestiondeMensajes" })
	public String GestiondePlantas() {
		return "/personal/GestiondeMensajes";

	}

	@GetMapping("/GestionMensajesMenuAdmin")
	public String mostrarMenuAdmin(HttpSession session) {
		String rol = (String) session.getAttribute("rol");
		System.out.println("ROL: " + rol);

		if (rol.equalsIgnoreCase("admin")) {
			return "/admin/MenuAdmin";
		} else {
			if (rol.equalsIgnoreCase("personal")) {
				return "/personal/MenuPersonal";
			} else {
				return "/inicio";
			}
		}

	}
}
