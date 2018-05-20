package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.service.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "admin")
public class AdminPanelController {

    @Autowired
    private UserService userService;

    private static final Logger LOGGER = Logger.getLogger(RestController.class);

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
