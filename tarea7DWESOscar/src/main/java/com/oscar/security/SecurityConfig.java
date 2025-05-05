package com.oscar.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends  {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(auth -> auth

				.requestMatchers("/", "/css/**", "/img/**").permitAll()

				.requestMatchers("/log/**", "/registro").permitAll()

				.requestMatchers("/cliente/**").hasRole("CLIENTE").requestMatchers("/admin/**").hasRole("ADMIN")
				.requestMatchers("/personal/**").hasAnyRole("ADMINISTRADOR", "PERSONAL")

				.anyRequest().permitAll()).formLogin(form -> form.loginPage("/inicio").loginProcessingUrl("/login")

						.defaultSuccessUrl("/Sesion")

						.failureUrl("/inicio").permitAll())
				.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/inicio").permitAll())
				.exceptionHandling(exception -> exception.accessDeniedPage("/inicio"));

		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http, CustomUserDetailsService uds)
			throws Exception {
		AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
		authBuilder.userDetailsService(uds).passwordEncoder(passwordEncoder());
		return authBuilder.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}