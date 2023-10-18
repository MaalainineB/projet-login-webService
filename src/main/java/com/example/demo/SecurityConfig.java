package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.filter.JwtAuthFilter;
import com.example.demo.service.UserInfoService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	@Autowired
	private JwtAuthFilter authFilter;

	// User Creation
	@Bean
	public UserDetailsService userDetailsService() {
		return new UserInfoService();
	}

	// Configuring HttpSecurity
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http.csrf(csrf -> csrf.disable()) //on disactive le csrf, car les tokens CSRF ne sont pas nécessaires pour une authentification sans état.
				.authorizeHttpRequests(ar -> ar.requestMatchers("/auth/addNewUser", "/auth/generateToken","/auth/userdetails","/auth/refreshToken").permitAll()) // autorise l'accès à certains points d'accès spécifiques pour tout le monde
				.authorizeHttpRequests(ar ->ar.requestMatchers("/auth/user/**").authenticated()) // exige une authentification pour d'autres
				.authorizeHttpRequests(ar -> ar.requestMatchers("/auth/admin/**").authenticated())
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and() //aucune session côté serveur n'est créée
				.authenticationProvider(authenticationProvider()) //un fournisseur d'authentification personnalisé qui utilise un UserDetailsService
				.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class) //Ajoute le JwtAuthFilter avant le UsernamePasswordAuthenticationFilter. Ce filtre personnalisé traite les jetons JWT pour l'authentification.
				.build(); 
	}

	// chiffrer le mot de passe
	@Bean
	public PasswordEncoder passwordEncoder() { // est utilisé pour hacher et vérifier les mots de passe des utilisateurs.
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() { 
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(); // fournie par Spring Security qui est utilisée pour l'authentification basée sur une base de données, Elle permet de valider les informations d'identification de l'utilisateur en les comparant à celles stockées en base de données.
		authenticationProvider.setUserDetailsService(userDetailsService()); //on lui attribue  un service de gestion des utilisateurs. userDetailsService() est une méthode qui renvoie un bean de service qui implémente l'interface UserDetailsService. Ce service est responsable de la récupération des détails de l'utilisateur à partir d'une source de données (par exemple, une base de données) lors de la tentative d'authentification.
		authenticationProvider.setPasswordEncoder(passwordEncoder()); //pour hacher et vérifier les mots de passe des utilisateurs. 
		return authenticationProvider; //renvoie l'instance du DaoAuthenticationProvider configurée. Cela signifie que ce fournisseur d'authentification sera utilisé par Spring Security pour vérifier les informations d'identification des utilisateurs lors des tentatives d'authentification.
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception { //L'AuthenticationManager est utilisé pour gérer les demandes d'authentification.
		return config.getAuthenticationManager();
	}
	//Le gestionnaire d'authentification retourné sera utilisé pour gérer les demandes d'authentification dans l'application. Il peut être utilisé pour authentifier les utilisateurs, vérifier leurs informations d'identification et gérer les sessions d'authentification

}
