package com.oscar.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.oscar.vivero.servicios.ServicioAutenticacion;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**").permitAll() 
                .requestMatchers("/admin/**").hasRole("ADMIN") 
                .requestMatchers("/cliente/**").hasRole("CLIENTE") 
                .requestMatchers("/personal/**").hasAnyRole("ADMIN", "PERSONAL") 
                .anyRequest().authenticated() 
            )
            .formLogin(login -> login
                .loginPage("/login") 
                .loginProcessingUrl("/Sesion") 
                .defaultSuccessUrl("/redireccion", true) 
                .failureUrl("/login?error=true") 
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/inicio") 
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .sessionManagement(session -> session
                .maximumSessions(1) 
                .expiredUrl("/inicio") 
            );
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        // Using HttpSecurity to build the AuthenticationManager
        return ((SecurityBuilder<AuthenticationManager>) http.getSharedObject(AuthenticationManagerBuilder.class)
                   .userDetailsService(userDetailsService(new ServicioAutenticacion()))
                   .passwordEncoder(passwordEncoder()))
                   .build();
    }

    @Bean
    public UserDetailsService userDetailsService(ServicioAutenticacion servicioAutenticacion) {
        return servicioAutenticacion;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); 
    }
}
