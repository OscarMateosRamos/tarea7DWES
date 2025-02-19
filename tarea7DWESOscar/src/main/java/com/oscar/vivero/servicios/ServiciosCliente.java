package com.oscar.vivero.servicios;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oscar.vivero.modelo.Cliente;
import com.oscar.vivero.modelo.Persona;
import com.oscar.vivero.repositories.ClienteRepository;

@Service
public class ServiciosCliente {

	@Autowired
	ClienteRepository clienterepo;

	public boolean validarCliente(String nombre, String email) {

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

		List<Cliente> clientes = clienterepo.findAll();
		for (Cliente c : clientes) {
			if (c.getEmail().equals(email)) {
				System.out.println(c.getEmail());
				System.out.println("El email ya existe...." + email);
				return false;
			}
		}

		return true;

	}

	public void insertarPlanta(Cliente c) {
		clienterepo.saveAndFlush(c);
	}

}
