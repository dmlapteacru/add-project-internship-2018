package com.endava.addprojectinternship2018.controller;

import com.endava.addprojectinternship2018.model.Notification;
import com.endava.addprojectinternship2018.model.enums.NotificationCase;
import com.endava.addprojectinternship2018.model.enums.Role;
import com.endava.addprojectinternship2018.model.dto.CompanyDto;
import com.endava.addprojectinternship2018.model.dto.CustomerDto;
import com.endava.addprojectinternship2018.model.dto.UserDto;
import com.endava.addprojectinternship2018.model.enums.UserStatus;
import com.endava.addprojectinternship2018.security.config.LoginAuthenticationSuccessHandler;
import com.endava.addprojectinternship2018.service.CompanyService;
import com.endava.addprojectinternship2018.service.CustomerService;
import com.endava.addprojectinternship2018.service.NotificationService;
import com.endava.addprojectinternship2018.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private final CustomerService customerService;
    private final CompanyService companyService;
    private final LoginAuthenticationSuccessHandler loginAuthenticationSuccessHandler;
    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationService notificationService;

    private static final Logger LOGGER = Logger.getLogger(UserRegistrationController.class);
    private static final String PATTERN = "(.*<\\s*script\\s*>.*)|(.*alert\\s*\\(\\s*\\).*)";

    @Autowired
    public UserRegistrationController(CustomerService customerService, CompanyService companyService, LoginAuthenticationSuccessHandler loginAuthenticationSuccessHandler, SimpMessagingTemplate messagingTemplate, NotificationService notificationService) {
        this.customerService = customerService;
        this.companyService = companyService;
        this.loginAuthenticationSuccessHandler = loginAuthenticationSuccessHandler;
        this.messagingTemplate = messagingTemplate;
        this.notificationService = notificationService;
    }

    @GetMapping(value = "")
    public String showRegistrationForm() {
        return "redirect:/login";
    }

    @PostMapping(value = "")
    public String choiceRegistrationForm(@ModelAttribute(name = "choice") String choice){
        if (choice.equals("CUSTOMER")){
            return "redirect:/registration/customer";
        }
        if (choice.equals("COMPANY")){
            return "redirect:/registration/company";
        }
        return "redirect:/login";
    }

    @GetMapping(value = "customer")
    public String showCustomerRegistrationForm(Model model) {
        UserDto userDto = new UserDto();
        userDto.setRole(Role.CUSTOMER);
        userDto.setStatus(UserStatus.ACTIVE);
        CustomerDto customerDto = new CustomerDto();
        customerDto.setUserDto(userDto);
        model.addAttribute("customerDto", customerDto);
        model.addAttribute("update", false);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)){
            return "redirect:" + loginAuthenticationSuccessHandler.authenticatedRedirectDefaultPage(authentication);
        }
        return "registration/customer";
    }

    @GetMapping(value = "company")
    public String showCompanyRegistrationForm(Model model) {
        UserDto userDto = new UserDto();
        userDto.setRole(Role.COMPANY);
        userDto.setStatus(UserStatus.INACTIVE);
        CompanyDto companyDto = new CompanyDto();
        companyDto.setUserDto(userDto);
        model.addAttribute("companyDto", companyDto);
        model.addAttribute("update", false);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)){
            return "redirect:" + loginAuthenticationSuccessHandler.authenticatedRedirectDefaultPage(authentication);
        }
        return "registration/company";
    }


    @PostMapping(value = "company")
    public String registerCompany(@ModelAttribute("companyDto") @Valid CompanyDto companyDto,
                                  BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "registration/company";
        }

        if (companyDto.getName().matches(PATTERN)) {
            result.rejectValue("name", "name.error", "Name contains illegal characters");
            return "registration/company";
        }

        if (userService.getUserByUsername(companyDto.getUserDto().getUsername()).isPresent()) {
            result.rejectValue("username", "username.error", "Username is not unique");
            return "registration/company";
        }

        if (companyService.getCompanyByEmail(companyDto.getEmail()).isPresent()) {
            result.rejectValue("email", "email.error", "Email is not unique");
            return "registration/company";
        }

        notificationService.save(new Notification(NotificationCase.NEW_USER, "New company - "+companyDto.getName()+" registered.", "admin"));
        messagingTemplate.convertAndSendToUser("admin","/queue/messages", "NOTIFICATION");
        companyService.saveCompany(companyDto);
        LOGGER.info(String.format("new company %s was registered", companyDto.getName()));
        return "redirect:/login?error=reg_approval";

    }

    @PostMapping(value = "customer")
    public String registerCustomer(@ModelAttribute("customerDto") @Valid CustomerDto customerDto,
                                  BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "registration/customer";
        }

        if (customerDto.getFirstName().matches(PATTERN)) {
            result.rejectValue("firstName", "firstName.error", "First name contains illegal characters");
            return "registration/customer";
        }

        if (customerDto.getLastName().matches(PATTERN)) {
            result.rejectValue("lastName", "lastName.error", "Last name contains illegal characters");
            return "registration/customer";
        }

        if (customerService.getCustomerByEmail(customerDto.getEmail()).isPresent()) {
            result.rejectValue("email", "email.error", "Email is not unique");
            return "registration/customer";
        }

        if (userService.getUserByUsername(customerDto.getUserDto().getUsername()).isPresent()) {
            result.rejectValue("userDto.username", "username.error", "Username is not unique");
            return "registration/customer";
        }

        notificationService.save(new Notification(NotificationCase.NEW_USER, "New company - "+customerDto.getFirstName()
                                    +" "+
                                    customerDto.getLastName()+" registered.", "admin"));
        messagingTemplate.convertAndSendToUser("admin","/queue/messages", "NOTIFICATION");
        customerService.saveCustomer(customerDto);
        LOGGER.info(String.format("new customer %s %s was registered", customerDto.getFirstName(), customerDto.getLastName()));
        return "redirect:/login";
    }

}
