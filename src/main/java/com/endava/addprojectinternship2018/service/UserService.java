package com.endava.addprojectinternship2018.service;

import com.endava.addprojectinternship2018.dao.UserDao;
import com.endava.addprojectinternship2018.model.*;
import com.endava.addprojectinternship2018.model.dto.UserDto;
import com.endava.addprojectinternship2018.model.dto.UserWithProfileDto;
import com.endava.addprojectinternship2018.model.dto.UsersEmailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static com.endava.addprojectinternship2018.model.enums.UserStatus.ACTIVE;
import static com.endava.addprojectinternship2018.model.enums.UserStatus.INACTIVE;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private PasswordTokenService passwordTokenService;

    public Optional<User> getUserByUsername(String username) {
        return userDao.findUserByUsername(username);
    }

    public Optional<User> getUserById(int id) {
        return userDao.findById(id);
    }

    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    public User saveUser(UserDto userDto) {
        return userDao.save(convertUserDtoToUser(userDto));
    }

    public User convertUserDtoToUser(UserDto userDto) {
        User user = userDao.findById(userDto.getUserId())
                .orElseGet(User :: new);
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(userDto.getRole());
        user.setUserStatus(userDto.getStatus());
        return user;
    }

    public UserDto convertUserToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUserId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setRole(user.getRole());
        userDto.setStatus(user.getUserStatus());
        return userDto;
    }

    public void changeUserStatus(String username) {
        User user = userDao.findUserByUsername(username).get();
        if (user.getUserStatus() == ACTIVE) {
            user.setUserStatus(INACTIVE);
        } else user.setUserStatus(ACTIVE);
        userDao.save(user);
    }

    public List<UserWithProfileDto> getAllUsersWithProfile() {
        return userDao.findAllUsersWithProfile();
    }

    public void changeUserPassword(UserDto user){
        User oldUser = getUserByUsername(user.getUsername()).get();
        oldUser.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.save(oldUser);
        passwordTokenService.deleteToken(user.getUsername());
    }

//    public UsersEmailDto getUsersEmailByUsername(String username){
//       return userDao.findUsersEmailByUsername(username);
//    }
}
