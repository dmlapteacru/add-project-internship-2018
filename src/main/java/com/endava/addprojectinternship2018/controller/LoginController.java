package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.model.User;
import com.endava.addprojectinternship2018.security.config.LoginAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@org.springframework.stereotype.Controller
@RequestMapping(value = {"/", "login"})
public class LoginController {

    @Autowired
    private LoginAuthenticationSuccessHandler loginAuthenticationSuccessHandler;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String login(Model model){
        model.addAttribute("loginForm_", new User());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)){
            return "redirect:" + loginAuthenticationSuccessHandler.authenticatedRedirectDefaultPage(authentication);
        }
       return "login/login";
    }
}


