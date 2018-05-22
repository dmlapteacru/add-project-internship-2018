package com.endava.addprojectinternship2018.util;

import com.endava.addprojectinternship2018.model.Company;
import com.endava.addprojectinternship2018.model.Customer;
import com.endava.addprojectinternship2018.model.User;
import com.endava.addprojectinternship2018.model.dto.UserEmailDto;
import com.endava.addprojectinternship2018.service.CompanyService;
import com.endava.addprojectinternship2018.service.CustomerService;
import com.endava.addprojectinternship2018.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserUtil {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyService companyService;

    public Customer getCurrentCustomer() {
        Customer result = null;
        Optional<Customer> customerOptional = customerService.getCustomerByUserId(getCurrentUser().getId());
        if (customerOptional.isPresent()) {
            result = customerOptional.get();
        }
        return result;
    }

    public Company getCurrentCompany() {
        Company result = null;
        Optional<Company> companyOptional = companyService.getCompanyByUserId(getCurrentUser().getId());
        if (companyOptional.isPresent()) {
            result = companyOptional.get();
        }
        return result;
    }

    public User getCurrentUser() {
        String userName;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
        } else {
            userName = principal.toString();
        }
        Optional<User> userOptional = userService.getUserByUsername(userName);
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException("No user with username: " + userName);
        }
        return userOptional.get();
    }

    public UserEmailDto getCurrentUserEmail() {
        return userService.getUserEmailByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    }

}
