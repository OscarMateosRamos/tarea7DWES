package com.oscar.vivero;

import java.util.ArrayList;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.oscar.vivero.modelo.Credenciales;
import com.oscar.vivero.modelo.LineaPedido;
import com.oscar.vivero.servicios.ServiciosCredenciales;

import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {

	@Autowired
	ServiciosCredenciales servCredenciales;

	@GetMapping({ "/", "inicio" })
	public String MenuInvitado(Model model, HttpSession session) {
//		String usuario = (String) session.getAttribute("usuario");
//		System.out.println("Sesion del usuario "+ usuario +".");
		return "inicio";
	}

	@GetMapping("/MenuPersonal")
	public String mostrarMenuPersonal(Model model, HttpSession session) {
		return "/personal/MenuPersonal";
	}

	@GetMapping("/MenuAdmin")
	public String mostrarMenuAdmin() {
		return "/admin/MenuAdmin";
	}

	@GetMapping("/MenuCliente")
	public String mostrarMenuCliente() {
		return "cliente/MenuCliente";
	}

	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("credenciales", new Credenciales());
		return "/log/formularioLogin";
	}

	@GetMapping("/redireccion")
	public String redireccionPorRol(HttpSession session) {
		org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null) {

			String username = auth.getName();

			String rol = "";
			for (GrantedAuthority authority : auth.getAuthorities()) {
				rol = authority.getAuthority();
			}

			session.setAttribute("usuario", username);
			session.setAttribute("rol", rol.replace("ROLE_", "").toLowerCase());
			session.setAttribute("lista", new ArrayList<LineaPedido>());

			switch (rol) {
			case "ROLE_ADMIN":
				return "redirect:/MenuAdmin";
			case "ROLE_PERSONAL":
				return "redirect:/MenuPersonal";
			case "ROLE_CLIENTE":
				return "redirect:/MenuCliente";
			default:
				return "redirect:/login?error=rol";
			}
		}

		return "redirect:/login?error=auth";
	}

	@GetMapping("/logout")
	public String cerrarSesion(HttpSession session) {
		System.out.println("Cerrando sesión...");
		session.invalidate();
		System.out.println("Sesión invalidada.");
		return "redirect:/inicio";
	}

}
