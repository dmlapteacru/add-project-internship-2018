package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.model.dto.CompanyRegistrationDto;
import com.endava.addprojectinternship2018.model.dto.CustomerRegistrationDto;
import com.endava.addprojectinternship2018.model.dto.UserRegistrationDto;
import com.endava.addprojectinternship2018.service.CompanyService;
import com.endava.addprojectinternship2018.service.CustomerService;
import com.endava.addprojectinternship2018.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "registration")
public class UserRegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CompanyService companyService;

    @GetMapping(value = "")
    public String showRegistrationForm() {
        return "registration/commonForm";
    }

    @GetMapping(value = "customer")
    public String showCustomerRegistrationForm(Model model) {
        model.addAttribute("user", new CustomerRegistrationDto());
        return "registration/customer";
    }

    @GetMapping(value = "company")
    public String showCompanyRegistrationForm(Model model) {
        model.addAttribute("user", new CompanyRegistrationDto());
        return "registration/company";
    }

    @PostMapping(value = "company")
    public String registerCompany(@ModelAttribute("user") @Valid CompanyRegistrationDto user,
                                  BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "registration/company";
        }

        if (userService.findUserByUsername(user.getUsername()).isPresent()) {
            result.rejectValue("username", "username.error", "Username is not unique");
            return "registration/company";
        }

        if (companyService.getCompanyByEmail(user.getEmail()).isPresent()) {
            result.rejectValue("email", "email.error", "Email is not unique");
            return "registration/company";
        }

        userService.saveUser(user);
        return "redirect:/registration/company?success";

    }

    @PostMapping(value = "customer")
    public String registerCustomer(@ModelAttribute("user") @Valid CustomerRegistrationDto user,
                                  BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "registration/customer";
        }

        if (userService.findUserByUsername(user.getUsername()).isPresent()) {
            result.rejectValue("username", "username.error", "Username is not unique");
            return "registration/customer";
        }

        if (customerService.getCustomerByEmail(user.getEmail()).isPresent()) {
            result.rejectValue("email", "email.error", "Email is not unique");
            return "registration/customer";
        }

        userService.saveUser(user);
        return "redirect:/registration/customer?success";
    }

}
