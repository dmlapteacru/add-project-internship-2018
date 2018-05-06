package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.model.User;
import com.endava.addprojectinternship2018.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


@org.springframework.web.bind.annotation.RestController
public class UserRestController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<User> all(){
        return userService.getAllUsers();
    }
}
