package com.endava.addprojectinternship2018;

import com.endava.addprojectinternship2018.security.config.LoginAuthenticationFailureHandler;
import com.endava.addprojectinternship2018.security.config.LoginAuthenticationSuccessHandler;
import com.endava.addprojectinternship2018.service.EmailServiceImpl;
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
	@Bean
	public LoginAuthenticationFailureHandler loginAuthenticationFailureHandler(){
		return new LoginAuthenticationFailureHandler();
	}
	@Bean
	public EmailServiceImpl emailService(){
		return new EmailServiceImpl();
	}
}
