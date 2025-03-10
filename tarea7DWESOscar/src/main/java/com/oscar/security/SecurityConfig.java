package com.oscar.security;


import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

	    public SecurityConfig(DataSource dataSource) {
	        this.dataSource = dataSource;
	    }
	
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/MostrarSesion","/Sesion", "/css/**", "/js/**", "/images/**").permitAll()  
                .anyRequest().authenticated()  
            )
            .formLogin(login -> login
                .loginPage("/formularioLogIn")
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
}
