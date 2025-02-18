package com.oscar.vivero;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.oscar.vivero.modelo.Credenciales;
import com.oscar.vivero.modelo.Persona;
import com.oscar.vivero.servicios.ServiciosCredenciales;
import com.oscar.vivero.servicios.ServiciosPersona;

@Controller
public class PersonaController {
	@Autowired
	ServiciosPersona servPersona;

	@Autowired
	ServiciosCredenciales servCredenciales;

	@GetMapping("/personas")
	public String listarPersonas(Model model) {
		model.addAttribute("personas", servPersona.vertodasPersonas());
		return "listadodePersonas";
	}

	@PostMapping("/CamposPersona")
	public String InsertarPlanta(@ModelAttribute Persona CrearPesonas, Model model) {

		Persona p = new Persona();

		String nombre = CrearPesonas.getNombre();
		String email = CrearPesonas.getEmail();
		String usuario = CrearPesonas.getCredencial().getUsuario();
		String password = CrearPesonas.getCredencial().getPassword();

		if (servCredenciales.existeCredencial(usuario)) {
			model.addAttribute("error", "Ya existe el usuario: " + usuario);
			return "CrearPersonas";
		} else {
			boolean personaValida = servPersona.validarPersona(nombre, email);

			if (!personaValida) {
				model.addAttribute("error", "Datos no validos para la persona ");
				return "CrearPersonas";
			} else {
				Credenciales c = new Credenciales();

				c.setUsuario(usuario);
				c.setPassword(password);

				servCredenciales.insertarCredencial(c);

				p.setNombre(nombre);
				p.setEmail(email);
				p.setCredencial(c);

				servPersona.insertarPersona(p);

			}

		}

		return "/CrearPersonas";
	}

	@GetMapping("/mostrarCrearPersonas")
	public String mostrarFormulario(Model model) {
		model.addAttribute("persona", new Persona());
		return "CrearPersonas";
	}

}
