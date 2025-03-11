package com.oscar.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.oscar.vivero.modelo.Credenciales;
import com.oscar.vivero.servicios.ServiciosCredenciales;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final ServiciosCredenciales servCredenciales;

	@Autowired
	public CustomUserDetailsService(ServiciosCredenciales servCredenciales) {
		this.servCredenciales = servCredenciales;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Credenciales credenciales = servCredenciales.buscarCredencialPorUsuario(username);

		if (credenciales == null) {
			throw new UsernameNotFoundException("Usuario no encontrado");
		}

		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_" + credenciales.getRol().toUpperCase()));

		return new User(credenciales.getUsuario(), credenciales.getPassword(), authorities);
	}
}
