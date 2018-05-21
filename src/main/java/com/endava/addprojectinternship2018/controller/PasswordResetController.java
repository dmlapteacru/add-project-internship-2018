package com.endava.addprojectinternship2018.controller;


import com.endava.addprojectinternship2018.service.PasswordTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PasswordResetController {

    @Autowired
    private PasswordTokenService passwordTokenService;


    @RequestMapping(value = "/reset/password", method = RequestMethod.GET)
    public String resetPassword(@RequestParam String username, @RequestParam String token) throws Exception{
        if (passwordTokenService.isTokenActive(username, token)){
            return "admin/resetPassword";
        }
        return "redirect:/login";
    }
}
