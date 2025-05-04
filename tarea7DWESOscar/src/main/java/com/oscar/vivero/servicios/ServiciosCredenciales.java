package com.oscar.vivero.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.oscar.vivero.modelo.Credenciales;
import com.oscar.vivero.repositories.CredencialRepository;

@Service
public class ServiciosCredenciales {

	@Autowired
	private CredencialRepository credencialrepo;

	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public void insertarCredencial(Credenciales c) {

		String passwordCifrada = passwordEncoder.encode(c.getPassword());

		c.setPassword(passwordCifrada);

		credencialrepo.saveAndFlush(c);
	}

	public boolean validarUsuarioPassword(Credenciales c) {
		if (c.getUsuario().isEmpty() || c.getPassword().isEmpty()) {
			return false;
		}
		String regpassword = "^([A-Za-z0-9_!?¿+-]){3,}$";
		return c.getPassword().matches(regpassword);
	}

	public Credenciales buscarCredencialPorUsuario(String usuario) {
		return credencialrepo.findByUsuario(usuario);
	}

	public boolean verificaUsuario(String usuario, String password) {
		Credenciales cred = credencialrepo.findByUsuario(usuario);
		return cred != null && passwordEncoder.matches(password, cred.getPassword()); // Comparación segura
	}

	public void actualizarRol(String usuario, String rol) {
		Credenciales c = credencialrepo.findByUsuario(usuario);
		if (c != null) {
			c.setRol(rol);
			insertarCredencial(c);
		} else {
			System.out.println("Usuario no encontrado.");
		}
	}

	
	public boolean existeCredencial(String usuario) {
		return credencialrepo.existsByUsuario(usuario); 
	}
}
