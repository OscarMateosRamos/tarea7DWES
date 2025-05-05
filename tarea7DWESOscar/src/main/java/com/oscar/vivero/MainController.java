package com.oscar.vivero;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.oscar.vivero.modelo.CestaCompra;
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

	@PostMapping("/Sesion")
	public String logInSubmit(@ModelAttribute Credenciales formularioLogin, Model model, HttpSession session) {
		String usuario = formularioLogin.getUsuario();
		String password = formularioLogin.getPassword();

		if (usuario == null || password == null || usuario.trim().isEmpty() || password.trim().isEmpty()) {
			model.addAttribute("error", "Por favor, ingrese ambos campos.");
			return "/log/formularioLogin";
		}

		System.out.println("Usuario recibido: " + usuario);
		System.out.println("Contraseña recibida: " + password);

		Credenciales credencial = servCredenciales.buscarCredencialPorUsuario(usuario);
		if (credencial == null) {
			model.addAttribute("error", "Credenciales incorrectas");
			return "/log/formularioLogin";
		}

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		if (!passwordEncoder.matches(password, credencial.getPassword())) {
			model.addAttribute("error", "Credenciales incorrectas");
			return "/log/formularioLogin";
		}

		ArrayList<LineaPedido> lista = new ArrayList<LineaPedido>();
		
		
		String rol = credencial.getRol();
		System.out.println("Rol recibido: " + rol);

		session.setAttribute("usuario", usuario);
		session.setAttribute("rol", rol);
		session.setAttribute("lista", lista);

		System.out.println("Guardado en la sesión - Usuario: " + usuario);
		System.out.println("Guardado en la sesión - Rol: " + rol);

		switch (rol.toLowerCase()) {
		case "admin":
			System.out.println("--Bienvenido Admin--");
			return "/admin/MenuAdmin";
		case "personal":
			System.out.println("--Bienvenido Personal--");
			return "/personal/MenuPersonal";
		case "cliente":
			System.out.println("--Bienvenido Cliente--");

			return "/cliente/MenuCliente";
		default:
			System.out.println("--Rol no reconocido--");
			model.addAttribute("error", "Rol no reconocido.");
			return "/log/formularioLogin";
		}
	}

	@GetMapping("/logout")
	public String cerrarSesion(HttpSession session) {
		System.out.println("Cerrando sesión...");
		session.invalidate();
		System.out.println("Sesión invalidada.");
		return "redirect:/inicio";
	}

}
