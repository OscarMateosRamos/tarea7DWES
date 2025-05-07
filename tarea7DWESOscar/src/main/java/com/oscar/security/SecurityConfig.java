package com.oscar.security;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
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
	@Order(SecurityProperties.BASIC_AUTH_ORDER)
	public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
		http.authorizeHttpRequests(auth -> auth
				.requestMatchers(
						 "/CSS").permitAll()
				.requestMatchers("/MenuAdmin").hasRole("ADMIN")
				.requestMatchers("/cliente/**").hasRole("CLIENTE")
				.requestMatchers("/personal/**").hasAnyRole("ADMIN", "PERSONAL")
				.anyRequest().authenticated())
				.formLogin(login -> login.loginPage("/login").loginProcessingUrl("/Sesion")
						.defaultSuccessUrl("/redirect", true).failureUrl("/public/login?error=true").permitAll())
				.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/invitado")
						.invalidateHttpSession(true).deleteCookies("JSESSIONID").permitAll())
				.sessionManagement(session -> session.maximumSessions(1).expiredUrl("/invitado"))
				.exceptionHandling(
						exception -> exception.accessDeniedHandler((request, response, accessDeniedException) -> {
							request.getSession().setAttribute("prevPage", request.getHeader("Referer"));
							request.getSession().setAttribute("rol",
									request.isUserInRole("ADMIN") ? "ADMIN"
											: request.isUserInRole("PERSONAL") ? "PERSONAL"
													: request.isUserInRole("CLIENTE") ? "CLIENTE" : "INVITADO");
							response.sendRedirect("/public/acceso_perfiles");
						}));
		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
			PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder);
		return new ProviderManager(authProvider);
	}

	@Bean
	public UserDetailsService userDetailsService(ServicioAutenticacion servAutenticacion) {
		return servAutenticacion;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
