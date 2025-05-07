package com.oscar.vivero.servicios;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oscar.vivero.modelo.Credenciales;
import com.oscar.vivero.repositories.CredencialRepository;

@Service
public class ServicioAutenticacion implements UserDetailsService {
	@Autowired
	CredencialRepository credencialesRepo;

	public ServicioAutenticacion(CredencialRepository credencialesRepository) {
		this.credencialesRepo = credencialesRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Credenciales credenciales = credencialesRepo.findByUsuario(username);
		if (credenciales == null) {
			throw new UsernameNotFoundException("Usuario " + username + " no encontrado");
		}

		if (credenciales.getRol() == null) {
			throw new IllegalStateException("Usuario " + username + " no tiene un rol asignado.");
		}

		String roleName = credenciales.getRol();

		if (roleName == null || roleName.trim().isEmpty()) {
			throw new IllegalStateException("Usuario " + username + " tiene un rol vacío o nulo.");
		}

		List<GrantedAuthority> authorities = Collections
				.singletonList(new SimpleGrantedAuthority("ROLE_" + roleName.trim()));

		return new User(credenciales.getUsuario(), credenciales.getPassword(), authorities);
	}

	public String usuarioAutenticado() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof User) {
			return ((User) authentication.getPrincipal()).getUsername();
		}
		return null;
	}

}
