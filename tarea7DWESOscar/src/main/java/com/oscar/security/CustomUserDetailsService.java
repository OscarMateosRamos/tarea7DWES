package com.oscar.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService; // ✔ Usa la interfaz
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.oscar.vivero.modelo.Credenciales;
import com.oscar.vivero.repositories.CredencialRepository;
import com.oscar.vivero.servicios.ServiciosCredenciales;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final ServiciosCredenciales credencialesService;

	public CustomUserDetailsService(ServiciosCredenciales credencialesService ) {
		this.credencialesService = credencialesService;
	}

	@Override 
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Credenciales credencial = credencialesService.buscarCredencialPorUsuario(username);

		if (credencial == null) {
			throw new UsernameNotFoundException("Usuario no encontrado");
		}

		return User.builder()
			.username(credencial.getUsuario())
			.password(credencial.getPassword())
			.roles(credencial.getRol()) 
			.build();
	}
}
