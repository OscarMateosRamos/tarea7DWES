package com.oscar.vivero;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.oscar.vivero.modelo.Credenciales;
import com.oscar.vivero.servicios.Controlador;
import com.oscar.vivero.servicios.ServiciosCredenciales;

import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {

	@Autowired
	ServiciosCredenciales servCredenciales;

	@Autowired
	Controlador controlador;

	@GetMapping({ "/", "MenuInvitado" })
	public String MenuInvitado() {
		return "MenuInvitado";
	}

	@GetMapping("/MenuPersonal")
	public String mostarMenuPersonal() {
		return "MenuPersonal";
	}

	@GetMapping("/MenuAdmin")
	public String mostarMenuAdmin() {
		return "MenuAdmin";
	}
	
	@GetMapping("/MenuFiltradoMensajes")
	public String mostarMenuFiltarMensajes() {
		return "MenuFiltradoMensajes";
	}

	
	@GetMapping("/MostrarSesion")
	public String logIn(Model model) {
		model.addAttribute("credenciales", new Credenciales());
		return "formularioLogIn";
	}

	@PostMapping("/Sesion")
	public String logInSubmit(@ModelAttribute Credenciales formularioLogIn, Model model, HttpSession session) {

		String usuario = formularioLogIn.getUsuario();
		String password = formularioLogIn.getPassword();

		if (usuario == null || password == null || usuario.trim().isEmpty() || password.trim().isEmpty()) {
			model.addAttribute("error", "Por favor, ingrese ambos campos.");
			return "formularioLogIn";
		}

		boolean existeCred = servCredenciales.verificaUsuario(usuario, password);
		if (!existeCred) {
			model.addAttribute("error", "Las credenciales no existen.");
			return "formularioLogIn";
		}

		Credenciales c = servCredenciales.buscarCredencialPorUsuario(usuario);

		session.setAttribute("credenciales", c);
		session.setAttribute("usuario", usuario);

		String rol = c.getRol();

		if ("admin".equals(rol)) {
			System.out.println("--Bienvenido Admin--");
			controlador.setUsername(usuario);
			return "MenuAdmin";
		}

		if ("personal".equals(rol)) {
			System.out.println("--Bienvenido Personal--");
			controlador.setUsername(usuario);
			return "MenuPersonal";
		}

		if ("cliente".equals(rol)) {
			System.out.println("--Bienvenido Cliente--");
			controlador.setUsername(usuario);
			return "RealizarPedido";
		}

		System.out.println("--Rol no reconocido--");
		return "formularioLogIn";

	}

	@GetMapping("/CerrarSesion")
	public String cerrarSesion(HttpSession session) {
		session.invalidate();
		return "/MostrarSesion";
	}
}
