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
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserUtil {

    @Autowired
    private  CustomerService customerService;

    @Autowired
    private  UserService userService;

    @Autowired
    private  CompanyService companyService;

    public  Customer getCurrentCustomer() {
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

    public Company getCurrentCompany() {
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

    public User getCurrentUser() {
        User result = null;
        Optional<User> userOptional = userService.getUserByUsername(getPrincipal());
        if (userOptional.isPresent()) {
            result = userOptional.get();
        }
        return result;
    }

    private String getPrincipal() {
        String userName;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String principal_1 = SecurityContextHolder.getContext().getAuthentication().getName();

        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }

}
