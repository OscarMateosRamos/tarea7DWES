package com.oscar.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.oscar.vivero.modelo.Credenciales;
import com.oscar.vivero.repositories.CredencialRepository;

@Service
public class UserDetailsService {

    private final CredencialRepository credencialesRepository;

    public UserDetailsService(CredencialRepository credencialesRepository) {
        this.credencialesRepository = credencialesRepository;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Buscar el usuario en la base de datos
        Credenciales credencial = credencialesRepository.findByUsuario(username);
        
        if (credencial == null) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }

        // Crear un objeto UserDetails basado en la información del usuario
        return User.builder()
                .username(credencial.getUsuario())
                .password(credencial.getPassword()) // Suponiendo que la contraseña está cifrada
                .roles(credencial.getRol()) // Asigna el rol del usuario
                .build();
    }
}
