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

		Optional<Ejemplar> existenteEjemplar = ejemplarrepo.findByNombre(ej.getNombre());

		if (existenteEjemplar.isPresent()) {

			Ejemplar existing = existenteEjemplar.get();
			String nuevoNombre = ej.getNombre() + "_" + existing.getId();
			ej.setNombre(nuevoNombre);
			ejemplarrepo.saveAndFlush(ej);
		}

		Mensaje m = new Mensaje();
		LocalDate fechahora = LocalDate.now();
		Date date = Date.valueOf(fechahora);

		Optional<Persona> p = servPersona.buscarPorId(1L);
		if (p.isPresent()) {
			m.setPersona(p.get());
		}

		String mensaje = "Añadido nuevo ejemplar " + ej.getNombre() + " por " + controlador.getUsername() + " ("
				+ fechahora + " ).";
		m.setEjemplar(ej);
		m.setFechahora(date);
		m.setMensaje(mensaje);

		servMensaje.insertar(m);
	}

	@Transactional
	public List<Ejemplar> listaejemplaresPorTipoPlanta(String codigo) {
		return ejemplarrepo.ejemplaresPorTipoPlanta(codigo);
	}

	@Transactional
	public Ejemplar buscarPorNombre(String nombre) {
		return ejemplarrepo.findByNombre(nombre).orElse(null);
	}

	@Transactional
	public boolean existeIdEjemplar(Long id) {
		return ejemplarrepo.existsById(id);
	}

	@Transactional
	public boolean existeNombreEjemplar(String nombre) {
		return ejemplarrepo.existsByNombre(nombre);
	}

	@Transactional
	public List<Ejemplar> vertodosEjemplares() {
		return ejemplarrepo.findAll();
	}

	@Transactional
	public Ejemplar buscarPorId(Long id) {
		Optional<Ejemplar> ejemplarOptional = ejemplarrepo.findById(id);
		if (ejemplarOptional.isPresent()) {
			Ejemplar ejemplar = ejemplarOptional.get();

			if (!ejemplar.getMensajes().isEmpty()) {
				Hibernate.initialize(ejemplar.getMensajes());
			}
			return ejemplar;
		}
		return null;
	}

	public void actualizarEjemplarAlRealizarPedido(Ejemplar ejemplar, String mensaje) {
		ejemplar.setDisponible(false);
		ejemplar.setAnotacion(mensaje);
		ejemplarrepo.save(ejemplar);
	}

	public List<Ejemplar> obtenerEjemplaresDisponiblesPorPlanta(String codigoPlanta) {
		return ejemplarrepo.findByPlantaCodigoAndDisponibleTrue(codigoPlanta);
	}
}
