package com.oscar.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	 SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth

						.requestMatchers("/","/css/**", "/img/**").permitAll()

						.requestMatchers("/log/**", "/registro").permitAll()

						.requestMatchers("/cliente/**").hasRole("CLIENTE")
						.requestMatchers("/admin/**").hasRole("ADMIN")
						.requestMatchers("/personal/**").hasAnyRole("ADMINISTRADOR", "PERSONAL")
						
// Cualquier otra ruta es ....
						.anyRequest().permitAll())
				.formLogin(form -> form.loginPage("/inicio").loginProcessingUrl("/login")
																								
						.defaultSuccessUrl("/Sesion") 
														
						.failureUrl("/inicio") 
						.permitAll())
				.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/inicio").permitAll())
				.exceptionHandling(exception -> exception 
						.accessDeniedPage("/inicio"));

		return http.build();
	}
	
	 @Bean
   public AuthenticationManager authenticationManager(HttpSecurity http, CustomUserDetailsService uds) throws Exception {
       AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
       authBuilder.userDetailsService(uds).passwordEncoder(passwordEncoder());
       return authBuilder.build();
   }

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}

//package com.oscar.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig  {
//	
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//            .csrf(csrf -> csrf.disable())
//            .authorizeHttpRequests(auth -> auth
//                .requestMatchers("/", "/login", "/registro", "/css/**", "/js/**", "/images/**").permitAll()
//                .requestMatchers("/admin/**").hasRole("ADMIN")
//                .requestMatchers("/personal/**").hasAnyRole("PERSONAL", "ADMIN")
//                .requestMatchers("/cliente/**").hasRole("CLIENTE")
//                .anyRequest().authenticated()
//            )
//            .formLogin(form -> form
//                .loginPage("/login")
//                .defaultSuccessUrl("/inicio", true)
//                .permitAll()
//            )
//            .logout(logout -> logout
//                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                .logoutSuccessUrl("/inicio")
//                .invalidateHttpSession(true)
//                .clearAuthentication(true)
//                .permitAll()
//            )
//            .sessionManagement(session -> session
//                .invalidSessionUrl("/login?expired=true")
//                .sessionFixation(sessionFixation -> sessionFixation.migrateSession())
//                .maximumSessions(1)
//                .maxSessionsPreventsLogin(false)
//            );
//
//        return http.build();
//    }
//
// 
//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http, UserDetailsService uds) throws Exception {
//        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
//        authBuilder.userDetailsService(uds).passwordEncoder(passwordEncoder());
//        return authBuilder.build();
//    }
//
//    
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//    
//}
