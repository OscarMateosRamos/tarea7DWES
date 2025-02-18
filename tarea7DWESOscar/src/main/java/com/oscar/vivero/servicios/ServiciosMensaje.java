package com.oscar.vivero.servicios;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oscar.vivero.modelo.Mensaje;
import com.oscar.vivero.repositories.MensajeRepository;

@Service
public class ServiciosMensaje {
	@Autowired
	MensajeRepository mensajerepo;

	public void insertar(Mensaje m) {
		mensajerepo.saveAndFlush(m);
	}

	public boolean validarFecha(String fecha) {

		String patron = "^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2])-(\\d{4})$";

		if (!Pattern.matches(patron, fecha)) {
			return false;
		}

		SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy");
		formatoFecha.setLenient(false);
		try {
			formatoFecha.parse(fecha);
			return true; // Es válida
		} catch (ParseException e) {
			return false; // No es válida
		}
	}

	public List<Mensaje> verTodosMensajes() {
		List<Mensaje> mensajes = mensajerepo.findAll();

		for (Mensaje m : mensajes) {
			System.out.println("Id: " + m.getId() + " fecha: " + m.getFechahora() + " Mensaje : " + m.getMensaje()
					+ " Ejemplar: " + m.getEjemplar().getNombre() + " CREADO POR: " + m.getPersona().getNombre());
		}
		return mensajes;

	}

	public List<Mensaje> listamensajesPorIdEjemplar(Long id) {
		List<Mensaje> mensajes = mensajerepo.mensajesPorIdEjemplar(id);
		return mensajes;

	}

	public List<Mensaje> listamensajesPorIdPersona(Long id) {
		List<Mensaje> mensajes = mensajerepo.mensajesPorIdPersona(id);
		return mensajes;

	}

	public List<Mensaje> listamensajesPorCodigoPlanta(String codigo) {
		List<Mensaje> mensajes = mensajerepo.mensajesPorCodigoPlanta(codigo);
		return mensajes;

	}

//	public List<Mensaje> listamensajesPorFechas(String fechaInicial, String fechaFinal) {
//		List<Mensaje> mensajes = mensajerepo.mensajesPorFechas(fechaInicial, fechaFinal);
//		return mensajes;
//
//	}

}
