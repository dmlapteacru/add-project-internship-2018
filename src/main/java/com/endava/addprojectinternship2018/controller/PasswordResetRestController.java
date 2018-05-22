package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.model.PasswordToken;
import com.endava.addprojectinternship2018.model.dto.UserDto;
import com.endava.addprojectinternship2018.service.PasswordTokenService;
import com.endava.addprojectinternship2018.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
public class PasswordResetRestController {
    @Autowired
    private PasswordTokenService passwordTokenService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/resetPassword", method = POST)
    public String resetPassword(@RequestBody PasswordToken passwordToken) {
        passwordTokenService.save(passwordToken);
        return "OK";
    }

    @RequestMapping(value = "/newUserPassword", method = PUT)
    public String setNewPass(@RequestBody UserDto user) {
        userService.changeUserPassword(user);
        return "OK";
    }
}
