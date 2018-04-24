package com.endava.addprojectinternship2018.service.user;

import com.endava.addprojectinternship2018.dao.CompanyDao;
import com.endava.addprojectinternship2018.dao.CustomerDao;
import com.endava.addprojectinternship2018.dao.UserDao;
import com.endava.addprojectinternship2018.model.*;
import com.endava.addprojectinternship2018.model.dto.CompanyRegistrationDto;
import com.endava.addprojectinternship2018.model.dto.CustomerRegistrationDto;
import com.endava.addprojectinternship2018.model.dto.UserRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private CompanyDao companyDao;


    public Optional<User> findUserByUsername(String username){
        return userDao.findUserByUsername(username);
    }

    public List<User> findAllUsers(){
        return userDao.findAll();
    }

    public void saveUser(UserRegistrationDto userDto) {

        User user = new User(userDto.getUsername(),
                passwordEncoder.encode(userDto.getPassword()),
                UserStatus.INACTIVE);
        user.setRole(Role.COMPANY);
        userDao.save(user);

        if (userDto instanceof CompanyRegistrationDto) {
            CompanyRegistrationDto companyRegistrationDto = (CompanyRegistrationDto) userDto;
            Company company = new Company();
            company.setName(companyRegistrationDto.getName());
            company.setEmail(companyRegistrationDto.getEmail());
            company.setUser(user);
            companyDao.save(company);
        } else {
            CustomerRegistrationDto customerRegistrationDto = (CustomerRegistrationDto) userDto;
            Customer customer = new Customer(customerRegistrationDto.getFirstName(),
                    customerRegistrationDto.getLastName(), customerRegistrationDto.getEmail());
            customer.setUser(user);
            customerDao.save(customer);
        }

    }
}
