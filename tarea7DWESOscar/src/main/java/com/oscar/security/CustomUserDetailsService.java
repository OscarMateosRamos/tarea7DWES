package com.oscar.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService; // ✔ Usa la interfaz
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.oscar.vivero.modelo.Credenciales;
import com.oscar.vivero.servicios.ServiciosCredenciales;

import jakarta.servlet.http.HttpSession;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final ServiciosCredenciales credencialesService;

	@Autowired
	HttpSession session;

	public CustomUserDetailsService(ServiciosCredenciales credencialesService) {
		this.credencialesService = credencialesService;
	}

	@Override 
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("******  USERNAME  ******");
		
		Optional<Credenciales>  optionalCredencial = Optional.of(credencialesService.buscarCredencialPorUsuario(username));
		
		
		
		if (optionalCredencial.isPresent()) {
			session.setAttribute(username, optionalCredencial.get().getUsuario());
			
			return User.builder()
					.username(optionalCredencial.get().getUsuario())
					.password(optionalCredencial.get().getPassword())
					.roles(optionalCredencial.get().getRol())
					.build();

		
		}else {
			
			throw new UsernameNotFoundException("Usuario no encontrado");
		}
		
		
	}
}
