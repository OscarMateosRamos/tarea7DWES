package com.oscar.security;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final DataSource dataSource;
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(DataSource dataSource, CustomUserDetailsService customUserDetailsService) {
        this.dataSource = dataSource;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http // /admin solo accede admin  /personal solo personal y /cliente solo cliente
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/MostrarSesion", "/Sesion", "/css/**", "/js/**", "/images/**").permitAll() 
                .requestMatchers("/admin/**").hasRole("ADMIN")  
                .requestMatchers("/personal/**").hasRole("PERSONAL") 
                .requestMatchers("/cliente/**").hasRole("CLIENTE")  
                
            )
            .formLogin(form -> form
                .loginPage("/MenuInvitado")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/CerrarSesion"))  
                .logoutSuccessUrl("/MenuInvitado")  
                .invalidateHttpSession(true) 
                .deleteCookies("JSESSIONID")  
                .permitAll()
            )
            .sessionManagement(session -> session
                .maximumSessions(1)  
                .maxSessionsPreventsLogin(true)  
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = 
            http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
            .userDetailsService(customUserDetailsService) 
            .passwordEncoder(passwordEncoder()); 

        return authenticationManagerBuilder.build();
    }
}
