package com.endava.addprojectinternship2018.util;

import com.endava.addprojectinternship2018.model.Company;
import com.endava.addprojectinternship2018.model.Customer;
import com.endava.addprojectinternship2018.model.User;
import com.endava.addprojectinternship2018.service.CompanyService;
import com.endava.addprojectinternship2018.service.CustomerService;
import com.endava.addprojectinternship2018.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class UserUtil {

    @Autowired
    private static CustomerService customerService;

    @Autowired
    private static UserService userService;

    @Autowired
    private static CompanyService companyService;

    public static Customer getCurrentCustomer() {
        Customer result = null;
        User currentUser = getCurrentUser();
        if (currentUser != null) {
            Optional<Customer> customerOptional = customerService.getCustomerByUserId(currentUser.getId());
            if (customerOptional.isPresent()) {
                result = customerOptional.get();
            }
        }
        return result;
    }

    public static Company getCurrentCompany() {
        Company result = null;
        User currentUser = getCurrentUser();
        if (currentUser != null) {
            Optional<Company> companyOptional = companyService.getCompanyByUserId(currentUser.getId());
            if (companyOptional.isPresent()) {
                result = companyOptional.get();
            }
        }
        return result;
    }

    public static User getCurrentUser() {
        User result = null;
        Optional<User> userOptional = userService.getUserByUsername(getPrincipal());
        if (userOptional.isPresent()) {
            result = userOptional.get();
        }
        return result;
    }

    private static String getPrincipal() {
        String userName;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }

}
