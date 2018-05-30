package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.model.Customer;
import com.endava.addprojectinternship2018.model.User;
import com.endava.addprojectinternship2018.service.UserService;
import com.endava.addprojectinternship2018.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@org.springframework.web.bind.annotation.RestController
public class UserRestController {

    private final UserService userService;
    private UserUtil userUtil;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setUserUtil(UserUtil userUtil) {
        this.userUtil = userUtil;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<User> all(){
        return userService.getAllUsers();
    }

    @RequestMapping(value = "/get/username", method = RequestMethod.GET)
    public String getUserName(){
        Customer customer = userUtil.getCurrentCustomer();
        if (customer!=null){
            return customer.getFullName();
        } else return userUtil.getCurrentCompany().getName();
    }
}
