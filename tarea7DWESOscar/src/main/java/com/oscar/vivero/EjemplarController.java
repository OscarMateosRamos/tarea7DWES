package com.oscar.vivero;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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

import jakarta.transaction.Transactional;

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

			if (ej.getMensajes() == null) {
				ej.setMensajes(new ArrayList<>());
			}

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
		List<Planta> plantas = servPlanta.vertodasPlantas();
		model.addAttribute("plantas", plantas);

		List<Ejemplar> ejemplares;
		if (codigo != null && !codigo.isEmpty()) {
			ejemplares = servEjemplar.listaejemplaresPorTipoPlanta(codigo);
		} else {
			ejemplares = servEjemplar.vertodosEjemplares();
		}

		model.addAttribute("ejemplares", ejemplares);
		model.addAttribute("codigoPlanta", codigo);

		return "listadoEjemplaresTipoPlanta";
	}

	@GetMapping("/verMensajesEjemplar")
	@Transactional
	public String verMensajesDelEjemplar(@RequestParam(required = false) Long ejemplarId, Model model) {

		List<Ejemplar> ejemplares = servEjemplar.vertodosEjemplares();
		model.addAttribute("ejemplares", ejemplares);

		if (ejemplarId != null) {
			Ejemplar ejemplar = servEjemplar.buscarPorId(ejemplarId);

			if (ejemplar == null) {
				model.addAttribute("error", "Ejemplar no encontrado");
			} else {
				List<Mensaje> mensajes = ejemplar.getMensajes();
				if (mensajes.isEmpty()) {
					model.addAttribute("mensajeError", "Este ejemplar no tiene mensajes.");
				} else {
					model.addAttribute("mensajes", mensajes);
				}
				model.addAttribute("ejemplar", ejemplar);
			}
		} else {

			List<Mensaje> todosLosMensajes = servMensaje.verTodosMensajes();
			model.addAttribute("mensajes", todosLosMensajes);
		}

		return "verMensajesEjemplar";
	}

}
