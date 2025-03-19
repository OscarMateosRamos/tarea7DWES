package com.oscar.security;

import java.util.ArrayList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.oscar.vivero.modelo.Credenciales;
import com.oscar.vivero.servicios.ServiciosCredenciales;

@Service
public class UserDetailsService {

    private final ServiciosCredenciales servCredenciales;

    // Constructor injection of the ServiciosCredenciales service
    @Autowired
    public UserDetailsService(ServiciosCredenciales servCredenciales) {
        this.servCredenciales = servCredenciales;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch user credentials from the database
        Credenciales credenciales = servCredenciales.buscarCredencialPorUsuario(username);

        // If user is not found, throw an exception
        if (credenciales == null) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }

        // Create authorities based on the user's role
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + credenciales.getRol().toUpperCase()));

        // Return a UserDetails object with the user's username, password, and roles
        return new User(credenciales.getUsuario(), credenciales.getPassword(), authorities);
    }
}
