package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "contact")
public class ContactAdminController {

    @Autowired
    private UserUtil userUtil;

    @GetMapping(value = {"/","admin"})
    public String contactAdmin(Model model){
        model.addAttribute("user", userUtil.getCurrentUserEmail().getEmail());
        return "admin/contact";
    }
}