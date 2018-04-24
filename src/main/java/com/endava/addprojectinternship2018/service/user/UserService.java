package com.endava.addprojectinternship2018.service.user;

import com.endava.addprojectinternship2018.dao.UserDao;
import com.endava.addprojectinternship2018.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public Optional<User> findUserByUsername(String username){
        return userDao.findUserByUsername(username);
    }

    public List<User> findAllUsers(){
        return userDao.findAll();
    }

    public void saveUser(User newUser) {
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        userDao.save(newUser);
    }
}
