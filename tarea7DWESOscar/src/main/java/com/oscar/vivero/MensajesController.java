package com.oscar.vivero;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.oscar.vivero.modelo.Ejemplar;
import com.oscar.vivero.modelo.Mensaje;
import com.oscar.vivero.modelo.Persona;
import com.oscar.vivero.modelo.Planta;
import com.oscar.vivero.servicios.Controlador;
import com.oscar.vivero.servicios.ServiciosEjemplar;
import com.oscar.vivero.servicios.ServiciosMensaje;
import com.oscar.vivero.servicios.ServiciosPersona;

@Controller
public class MensajesController {

	@Autowired
	ServiciosMensaje servMensaje;

	@Autowired
	ServiciosPersona servPersona;

	@Autowired
	ServiciosEjemplar servEjemplar;

	@Autowired
	Controlador controlador;

	@PostMapping("/CamposMensaje")
	public String InsertarMensaje(@RequestParam Long id, @RequestParam String mensaje, @RequestParam String fechahora,
			Model model) {

		if (id == null || id == 0) {
			model.addAttribute("error", "Debe seleccionar un ejemplar.");
			return "CrearMensaje";
		}

		if (!servEjemplar.existeIdEjemplar(id)) {
			model.addAttribute("error", "No existe el idEjemplar: " + id);
			return "CrearMensaje";
		}

		Persona p = servPersona.buscarPorNombre(controlador.getUsername());
		Ejemplar ej = servEjemplar.buscarPorId(id);

		Date fechaHoraDate;
		fechaHoraDate = Date.valueOf(fechahora);

		Mensaje m = new Mensaje();
		m.setFechahora(fechaHoraDate);
		m.setMensaje(mensaje);
		m.setEjemplar(ej);
		m.setPersona(p);

		servMensaje.insertar(m);

		return "CrearMensaje";
	}

	@GetMapping("/mostrarCrearMensajes")
	public String mostrarCrearMensajeFormulario(Model model) {

		model.addAttribute("mensaje", new Mensaje());

		List<Ejemplar> ejemplares = servEjemplar.vertodosEjemplares();

		model.addAttribute("ejemplares", ejemplares);

		return "CrearMensaje";

	}

	@GetMapping("/mensajes")
	public String listarMensajes(Model model) {
		List<Mensaje> m = servMensaje.verTodosMensajes();
		model.addAttribute("mensajes", m);
		return "listadodeMensajes";
	}

}
