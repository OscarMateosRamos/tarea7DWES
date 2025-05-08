package com.oscar.vivero.servicios;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oscar.vivero.modelo.Credenciales;
import com.oscar.vivero.repositories.CredencialRepository;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class ServicioAutenticacion implements UserDetailsService {

    @Autowired
    private CredencialRepository credencialesRepo;

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

        String roleName = credenciales.getRol().trim();
        if (roleName.isEmpty()) {
            throw new IllegalStateException("Usuario " + username + " tiene un rol vacío o nulo.");
        }

        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + roleName));
        return new User(credenciales.getUsuario(), credenciales.getPassword(), authorities);
    }

  
    public String usuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return ((User) authentication.getPrincipal()).getUsername();
        }
        return null;
    }


    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            try {             
                redirectUserBasedOnRole(authentication, response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

  
    private void redirectUserBasedOnRole(Authentication authentication, HttpServletResponse response) throws IOException {
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                response.sendRedirect("/admin/MenuAdmin");
                return;
            } else if (authority.getAuthority().equals("ROLE_CLIENTE")) {
                response.sendRedirect("/cliente/MenuCliente");
                return;
            } else if (authority.getAuthority().equals("ROLE_PERSONAL")) {
                response.sendRedirect("/personal/MenuPersonal");
                return;
            }
        }
      
        response.sendRedirect("/invitado");
    }
}
