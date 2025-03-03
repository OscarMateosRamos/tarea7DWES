package com.oscar.vivero.servicios;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oscar.vivero.modelo.Ejemplar;
import com.oscar.vivero.modelo.Mensaje;
import com.oscar.vivero.modelo.Persona;
import com.oscar.vivero.repositories.EjemplarRepository;

@Service
public class ServiciosEjemplar {

	@Autowired
	EjemplarRepository ejemplarrepo;

	@Autowired
	ServiciosPersona servPersona;

	@Autowired
	ServiciosMensaje servMensaje;

	@Autowired
	Controlador controlador;

	@Transactional
	public void insertarEjemplar(Ejemplar ej) {
		ejemplarrepo.saveAndFlush(ej);

		List<Ejemplar> ejemplares = ejemplarrepo.findAll();

		for (Ejemplar e : ejemplares) {
			if (e.getNombre().equals(ej.getNombre())) {
				String nuevoNombre = ej.getNombre() + "_" + e.getId();
				ej.setNombre(nuevoNombre);
				ejemplarrepo.saveAndFlush(ej);

				Mensaje m = new Mensaje();

				LocalDate fechahora = LocalDate.now();
				Date date = Date.valueOf(fechahora);

				Optional<Persona> p = servPersona.buscarPorId((long) 1);

				String mensaje = "Añadido nuevo ejemplar " + ej.getNombre() + " por " + controlador.getUsername() + " ("
						+ fechahora + " ).";
				m.setEjemplar(ej);
				m.setFechahora(date);
				m.setMensaje(mensaje);

				Optional<Persona> personas = servPersona.buscarPorId(Long.valueOf(1));
				m.setPersona(personas.get());

				servMensaje.insertar(m);
			}
		}
	}

	public List<Ejemplar> listaejemplaresPorTipoPlanta(String codigo) {
		List<Ejemplar> ejemplares = ejemplarrepo.ejemplaresPorTipoPlanta(codigo);
		return ejemplares;
	}

	public Ejemplar buscarPorNombre(String nombre) {
		Ejemplar ej = new Ejemplar();
		List<Ejemplar> ejemplares = ejemplarrepo.findAll();
		for (Ejemplar e : ejemplares) {
			if (e.getNombre().equals(nombre)) {
				ej = e;
			}
		}
		return ej;
	}

	public boolean existeIdEjemplar(Long id) {
		return ejemplarrepo.existsById(id);
	}

	public boolean existeNombreEjemplar(String nombre) {
		return ejemplarrepo.existsByNombre(nombre);
	}

	public List<Ejemplar> vertodosEjemplares() {
		return ejemplarrepo.findAll();
	}

	public Ejemplar buscarPorId(Long id) {
		Optional<Ejemplar> ejemplarOptional = ejemplarrepo.findById(id);
		Ejemplar ejemplar = ejemplarOptional.orElse(null);

		if (ejemplar != null) {

			Hibernate.initialize(ejemplar.getMensajes());
		}

		return ejemplar;
	}
}
