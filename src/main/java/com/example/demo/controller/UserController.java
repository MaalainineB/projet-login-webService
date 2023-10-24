package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.UserInfoDto;
import com.example.demo.dto.UserInfoMapper;
import com.example.demo.entity.AuthRequest;
import com.example.demo.entity.UserInfo;
import com.example.demo.service.JwtService;
import com.example.demo.service.UserInfoService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
//@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

	@Autowired
	private UserInfoMapper dtoMapper;

	@Autowired
	private UserInfoService service;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@PostMapping("/addNewUser")
	public String addNewUser(@RequestBody UserInfo userInfo) {
		return service.addUser(userInfo);
	}

//	@GetMapping("/user/userProfile") 
//	@PreAuthorize("hasAuthority('ROLE_USER')") 
//	public String userProfile() { 
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
////        this.service.loadUserByUsername(username)
//        System.out.println(username);
//		return username;
//	} 

	@GetMapping("/userdetails") //gère une requête GET à l'URL "/userdetails"
	@PreAuthorize("hasAuthority('ROLE_USER')") // une autorisation requise pour accéder à cette méthode.
	public UserInfoDto findByUsername(HttpServletRequest request) { //accepte un objet HttpServletRequest comme argument, ce qui lui permet d'accéder aux informations de la requête HTTP entrante.
		String authorizationHeader = request.getHeader("Authorization"); //extraire l'en-tête "Authorization" de la requête HTTP 
		if (authorizationHeader != null & authorizationHeader.startsWith("Bearer ")) {
			String token = authorizationHeader.substring(7);
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //pour obtenir l'objet d'authentification actuel. Cet objet d'authentification devrait contenir des informations sur l'utilisateur actuellement authentifié.
			String username = authentication.getName();
			UserInfo existingUserInfo = service.loadByUsername(username);
			UserInfoDto existingUserDto = dtoMapper.toDto(existingUserInfo);
			return existingUserDto;
		} else {
			return null;
		}

	}

	@GetMapping("/admin/adminProfile")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public String adminProfile() {
		return "Welcome to Admin Profile";
	}

//	@PostMapping("/generateToken")
//	public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
//		Authentication authentication = authenticationManager.authenticate(
//				new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
//		if (authentication.isAuthenticated()) {
//			return jwtService.generateToken(authRequest.getUsername());
//		} else {
//			throw new UsernameNotFoundException("invalid user request !");
//		}
//	}
	
	@PostMapping("/generateToken")
	public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
	    try {
	        Authentication authentication = authenticationManager.authenticate(
	            new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

	        return jwtService.generateToken(authRequest.getUsername());
	    } catch (AuthenticationException e) {
	        throw new UsernameNotFoundException("Nom d'utilisateur ou mot de passe incorrect");
	    }
	}

	
	@PostMapping("/refreshToken")
	public String refreshToken(HttpServletRequest request) {
		System.err.println("je suis là");
		String authorizationHeader = request.getHeader("Authorization"); //extraire l'en-tête "Authorization" de la requête HTTP 
			String currentToken = authorizationHeader.substring(7);
			String currentUsername = jwtService.extractUsername(currentToken);
			Boolean isTokentValid = jwtService.validateTokenButExpird(currentToken, currentUsername);
			if (isTokentValid) {
	            // Générez un nouveau token JWT avec la même identité
//	            UsernamePasswordAuthenticationToken newToken = new UsernamePasswordAuthenticationToken(existingUserDetail.getUsername(),existingUserDetail.getPassword());
	            String newToken = jwtService.generateToken(currentUsername);
	            return newToken;
			} else {
	            return "Le token actuel est invalide.";
			}
		
	}

}
