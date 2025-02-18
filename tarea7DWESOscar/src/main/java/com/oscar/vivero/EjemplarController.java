package com.oscar.vivero;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.oscar.vivero.modelo.Ejemplar;
import com.oscar.vivero.modelo.Mensaje;
import com.oscar.vivero.modelo.Planta;
import com.oscar.vivero.repositories.EjemplarRepository;
import com.oscar.vivero.servicios.Controlador;
import com.oscar.vivero.servicios.ServiciosEjemplar;
import com.oscar.vivero.servicios.ServiciosMensaje;
import com.oscar.vivero.servicios.ServiciosPersona;
import com.oscar.vivero.servicios.ServiciosPlanta;

@Controller
public class EjemplarController {

	@Autowired
	EjemplarRepository ejemplarrepo;

	@Autowired
	ServiciosPersona servPersona;
	@Autowired
	ServiciosMensaje servMensaje;
	@Autowired
	ServiciosEjemplar servEjemplar;
	@Autowired
	ServiciosPlanta servPlanta;
	@Autowired
	Controlador controlador;

	@PostMapping("/CamposEjemplar")
	public String InsertarEjemplar(@ModelAttribute Ejemplar CrearEjemplar, Model model) {

		String codigoPlanta = CrearEjemplar.getPlanta().getCodigo();

		if (servPlanta.existeCodigoPlanta(codigoPlanta)) {

			Ejemplar ej = new Ejemplar();

			List<Planta> plantas = servPlanta.encontrarPlantasPorCodigo(codigoPlanta);
			if (!plantas.isEmpty()) {
				ej.setPlanta(plantas.get(0));
			} else {
				model.addAttribute("error", "La planta con el nombre " + codigoPlanta + " no fue encontrada.");
				return "CrearEjemplar";
			}

			ej.setNombre(codigoPlanta);

			Mensaje mensaje = new Mensaje();
			mensaje.setMensaje("Ejemplar: " + ej.getNombre() + " creado con éxito");

			Mensaje m = new Mensaje();
			m.setMensaje("Ejemplar: " + ej.getNombre() + " creado con éxito");

			if (ej.getMensajes() == null) {
				ej.setMensajes(new ArrayList<>());
			}

			servMensaje.insertar(m);

			servEjemplar.insertarEjemplar(ej);

			return "CrearEjemplar";
		} else {
			model.addAttribute("error", "No existe el código de la planta.");
			return "CrearEjemplar";
		}
	}

	@GetMapping("/mostrarCrearEjemplar")
	public String mostrarCrearEjemplarFormulario(Model model) {

		List<Ejemplar> ejemplares = servEjemplar.vertodosEjemplares();
		model.addAttribute("ejemplares", ejemplares);

		Ejemplar ejemplar = new Ejemplar();
		model.addAttribute("ejemplar", ejemplar);

		return "CrearEjemplar";
	}

	@GetMapping("/ejemplaresTipoPlanta")
	public String listarEjemplaresTipoPlanta(@RequestParam(required = false) String codigo, Model model) {

		List<Ejemplar> ejemplares;

		if (codigo != null && !codigo.isEmpty()) {
			ejemplares = servEjemplar.listaejemplaresPorTipoPlanta(codigo);
		} else {
			ejemplares = servEjemplar.vertodosEjemplares();
		}

		List<Planta> plantas = servPlanta.vertodasPlantas();

		model.addAttribute("plantas", plantas);
		model.addAttribute("ejemplares", ejemplares);
		model.addAttribute("codigoPlantaSeleccionado", codigo);

		return "listadoEjemplaresTipoPlanta";
	}

	@GetMapping("/verMensajesEjemplar")
	public String verMensajesDeEjemplar(@PathVariable Long id, Model model) {

		Ejemplar ejemplar = servEjemplar.buscarPorId(id);

		if (ejemplar == null) {
			model.addAttribute("error", "Ejemplar no encontrado");
			return "error";
		}

		List<Mensaje> mensajes = ejemplar.getMensajes();

		model.addAttribute("ejemplar", ejemplar);
		model.addAttribute("mensajes", mensajes);

		return "verMensajesEjemplar";
	}

}
