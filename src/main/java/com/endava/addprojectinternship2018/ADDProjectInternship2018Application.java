package com.endava.addprojectinternship2018;

import com.endava.addprojectinternship2018.security.config.LoginAuthenticationSuccessHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ADDProjectInternship2018Application {

	public static void main(String[] args) {
		SpringApplication.run(ADDProjectInternship2018Application.class, args);
	}
	@Bean
	public LoginAuthenticationSuccessHandler loginAuthenticationSuccessHandler(){
		return new LoginAuthenticationSuccessHandler();
	}
}
