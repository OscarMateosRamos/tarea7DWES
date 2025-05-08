package com.oscar.security;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.oscar.vivero.modelo.Credenciales;
import com.oscar.vivero.repositories.CredencialRepository;

@Service
public class ServicioAutenticacion implements UserDetailsService {

    @Autowired
    private CredencialRepository credencialesRepo;

    public ServicioAutenticacion(CredencialRepository credencialesRepository) {
        this.credencialesRepo = credencialesRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       
        Credenciales credenciales = credencialesRepo.findByUsuario(username);

        if (credenciales == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }

       
        String roleName = credenciales.getRol();
        if (roleName == null || roleName.trim().isEmpty()) {
            throw new IllegalStateException("El usuario no tiene un rol asignado.");
        }

    
        return new User(credenciales.getUsuario(), credenciales.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + roleName.toUpperCase())));
    }
}
