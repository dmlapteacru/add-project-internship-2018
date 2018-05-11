package com.endava.addprojectinternship2018;

import com.endava.addprojectinternship2018.security.config.LoginAuthenticationFailureHandler;
import com.endava.addprojectinternship2018.security.config.LoginAuthenticationSuccessHandler;
import com.endava.addprojectinternship2018.service.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

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

	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}

	@Bean
	@Qualifier("bank_ip")
	public String bankIP(){
		return "http://172.17.100.255:82/api";
	}
}
