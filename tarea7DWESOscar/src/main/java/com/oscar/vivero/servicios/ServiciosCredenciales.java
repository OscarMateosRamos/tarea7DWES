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

	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // Añadido para cifrado de contraseñas

	public void insertarCredencial(Credenciales c) {
		// Cifra la contraseña antes de guardarla en la base de datos
		String encodedPassword = passwordEncoder.encode(c.getPassword());
		c.setPassword(encodedPassword); // Asignamos la contraseña cifrada
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

	// Método actualizado para comparar contraseñas cifradas
	public boolean verificaUsuario(String usuario, String password) {
		Credenciales cred = credencialrepo.findByUsuario(usuario);
		return cred != null && passwordEncoder.matches(password, cred.getPassword()); // Comparación segura
	}

	public void actualizarRol(String usuario, String rol) {
		Credenciales c = credencialrepo.findByUsuario(usuario);
		if (c != null) {
			c.setRol(rol);
			insertarCredencial(c); // Actualizamos la credencial
		} else {
			System.out.println("Usuario no encontrado.");
		}
	}

	// Método optimizado: consulta directa en la base de datos
	public boolean existeCredencial(String usuario) {
		return credencialrepo.existsByUsuario(usuario); // Método optimizado en el repositorio
	}
}
