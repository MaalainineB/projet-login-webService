package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class LoginAuthJwtApplication implements WebMvcConfigurer,CommandLineRunner {

//    private static Logger logger = Logger.getLogger(LoginAuthJwtApplication.class);

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public static void main(String[] args) {
		SpringApplication.run(LoginAuthJwtApplication.class, args);
//        logger.info("SpringBoot Start Success");
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**") // Allow all paths
				.allowedOrigins("http://localhost:4200") // Allow requests only from http://localhost:4200
				.allowedMethods("GET", "POST", "PUT", "DELETE") // Specify the allowed HTTP methods
				.allowedHeaders("*"); // Allow all headers
	}

	@Override
	public void run(String... args) throws Exception {
		String pass = "1234";
		System.err.println(passwordEncoder.encode(pass));
		
	}

}
