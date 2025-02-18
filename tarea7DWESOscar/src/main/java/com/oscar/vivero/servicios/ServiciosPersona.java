package com.oscar.vivero.servicios;

import java.util.List;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oscar.vivero.modelo.Persona;
import com.oscar.vivero.modelo.Planta;
import com.oscar.vivero.repositories.PersonaRepository;

@Service
public class ServiciosPersona {

	@Autowired
	PersonaRepository personarepo;

	public boolean validarPersona(String nombre, String email) {

		if (nombre.length() > 255) {
			System.out.println("Nombre invalido");
			return false;
		}

		if (email.length() > 255) {
			System.out.println("Email invalido");
			return false;
		}

		String patron = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
		Pattern pattern = Pattern.compile(patron);

		Matcher matcher = pattern.matcher(email);
		if (!matcher.matches()) {
			System.out.println(" Formato de email invalido..." + email);
			return false;
		}

		List<Persona> personas = personarepo.findAll();
		for (Persona p : personas) {
			if (p.getEmail().equals(email)) {
				System.out.println(p.getEmail());
				System.out.println("El email ya existe...." + email);
				return false;
			}
		}

		return true;

	}

	public List<Persona> vertodasPersonas() {
		List<Persona> plantas = personarepo.findAll();
		return plantas;
	}

	public void insertarPersona(Persona p) {
		personarepo.saveAndFlush(p);
	}

	public Optional<Persona> buscarPorId(Long id) {
		return personarepo.findById(id);
	}

	public Persona buscarPorNombre(String nombre) {
		Persona per = new Persona();

		List<Persona> personas = personarepo.findAll();

		for (Persona p : personas) {
			if (p.getCredencial().getUsuario().equals(nombre)) {

				per = p;
			}
		}

		return per;
	}

	public boolean existeidPersona(long idPersona) {
		List<Persona> personas = personarepo.findAll();

		for (Persona p : personas) {
			if (p.getId().equals(idPersona)) {

				return true;
			}
		}
		return false;
	}

	public boolean existeNombrePersona(String nombre) {
		List<Persona> personas = personarepo.findAll();

		for (Persona p : personas) {
			if (p.getNombre().equals(nombre)) {

				return true;
			}
		}
		return false;
	}
}
