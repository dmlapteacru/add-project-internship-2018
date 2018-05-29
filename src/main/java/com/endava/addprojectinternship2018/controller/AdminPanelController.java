package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "admin")
public class AdminPanelController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String adminMain(Model model){
        model.addAttribute("users", userService.getAllUsersWithProfile());
        return "admin/admin";
    }

    @RequestMapping(value = "status", method = RequestMethod.GET)
    public String approveNewUser(@RequestParam(name = "username")String username){
        userService.changeUserStatus(username);
        return "redirect:/admin";
    }

}
