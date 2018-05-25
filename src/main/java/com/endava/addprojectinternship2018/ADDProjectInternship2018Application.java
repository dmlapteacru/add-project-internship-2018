package com.endava.addprojectinternship2018;

import com.endava.addprojectinternship2018.security.config.LoginAuthenticationFailureHandler;
import com.endava.addprojectinternship2018.security.config.LoginAuthenticationSuccessHandler;
import com.endava.addprojectinternship2018.service.EmailServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

@SpringBootApplication
public class ADDProjectInternship2018Application extends SpringBootServletInitializer{

    @Value("${bank.connection.endpoint}")
    private String bankEndpoint;

    public static void main(String[] args) {
        SpringApplication.run(ADDProjectInternship2018Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ADDProjectInternship2018Application.class);
    }

    @Bean
    public LoginAuthenticationSuccessHandler loginAuthenticationSuccessHandler() {
        return new LoginAuthenticationSuccessHandler();
    }

    @Bean
    public LoginAuthenticationFailureHandler loginAuthenticationFailureHandler() {
        return new LoginAuthenticationFailureHandler();
    }

    @Bean
    public EmailServiceImpl emailService() {
        return new EmailServiceImpl();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @Qualifier("bank_ip")
    public String bankIP() {
        return bankEndpoint;
    }

    @Bean
    public KeyPairGenerator getKeyPairGenerator(@Qualifier("encryptionAlgorithm")String algorithm) throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(algorithm);
        keyGen.initialize(4096);
        return keyGen;
    }

    @Bean
    public KeyFactory getKeyFactory(@Qualifier("encryptionAlgorithm")String algorithm) throws NoSuchAlgorithmException {
        return KeyFactory.getInstance(algorithm);
    }

    @Bean
    @Qualifier("encrypter")
    public Cipher getEncrypter(@Qualifier("encryptionAlgorithm")String algorithm) throws NoSuchPaddingException, NoSuchAlgorithmException {
        return Cipher.getInstance(algorithm);
    }

    @Bean
    @Qualifier("decrypter")
    public Cipher getDecrypter(@Qualifier("encryptionAlgorithm")String algorithm) throws NoSuchPaddingException, NoSuchAlgorithmException {
        return Cipher.getInstance(algorithm);
    }

    @Bean
    @Qualifier("encryptionAlgorithm")
    public String algorithm(){
        return "RSA";
    }

    @Bean
    public ObjectMapper getObjectMapper(){
        return new ObjectMapper();
    }

}
