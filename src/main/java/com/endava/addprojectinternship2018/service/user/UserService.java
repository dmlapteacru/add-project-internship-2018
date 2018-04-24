package com.endava.addprojectinternship2018.service.user;

import com.endava.addprojectinternship2018.dao.CompanyDao;
import com.endava.addprojectinternship2018.dao.CustomerDao;
import com.endava.addprojectinternship2018.dao.UserDao;
import com.endava.addprojectinternship2018.model.*;
import com.endava.addprojectinternship2018.model.dto.CompanyRegistrationDto;
import com.endava.addprojectinternship2018.model.dto.CustomerRegistrationDto;
import com.endava.addprojectinternship2018.model.dto.UserRegistrationDto;
import com.endava.addprojectinternship2018.model.dto.UserWithProfileDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static com.endava.addprojectinternship2018.model.UserStatus.ACTIVE;
import static com.endava.addprojectinternship2018.model.UserStatus.INACTIVE;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private CompanyDao companyDao;


    public Optional<User> getUserByUsername(String username){
        return userDao.findUserByUsername(username);
    }

    public List<User> getAllUsers(){
        return userDao.findAll();
    }

    public void saveUser(UserRegistrationDto userDto) {

        User user = new User(userDto.getUsername(),
                passwordEncoder.encode(userDto.getPassword()),
                INACTIVE);
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

    public void changeUserStatus(String username){
        User user = userDao.findUserByUsername(username).get();
        if (user.getUserStatus() == ACTIVE){
            user.setUserStatus(INACTIVE);
        } else user.setUserStatus(ACTIVE);
        userDao.save(user);
    }

    public List<UserWithProfileDto> getAllUsersWithProfile() {
       return userDao.findAllUsersWithProfile();
    }
}
