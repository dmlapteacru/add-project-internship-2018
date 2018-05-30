package com.endava.addprojectinternship2018.service;

import com.endava.addprojectinternship2018.dao.UserDao;
import com.endava.addprojectinternship2018.exception.NoBankAccountException;
import com.endava.addprojectinternship2018.model.*;
import com.endava.addprojectinternship2018.model.dto.*;
import com.endava.addprojectinternship2018.model.enums.Role;
import com.endava.addprojectinternship2018.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.endava.addprojectinternship2018.model.enums.UserStatus.ACTIVE;
import static com.endava.addprojectinternship2018.model.enums.UserStatus.INACTIVE;

@Service
public class UserService {

    @Autowired
    private PasswordTokenService passwordTokenService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private WebSocketDistributeService webSocketDistributeService;

    public Optional<User> getUserByUsername(String username) {
        return userDao.findUserByUsername(username);
    }

    public Optional<User> getUserById(int id) {
        return userDao.findById(id);
    }

    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @Transactional
    public User saveUser(UserDto userDto) {
        return userDao.save(convertUserDtoToUser(userDto));
    }
    @Transactional
    public User save(User user) {
        return userDao.save(user);
    }

    public User convertUserDtoToUser(UserDto userDto) {
        User user = userDao.findById(userDto.getUserId())
                .orElseGet(User::new);
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

    @Transactional
    public void changeUserStatus(String username) {
        User user = userDao.findUserByUsername(username).get();
        if (user.getUserStatus() == ACTIVE) {
            user.setUserStatus(INACTIVE);
        } else user.setUserStatus(ACTIVE);
        userDao.save(user);
    }

    @Transactional
    public void changeUserStatusOnActive(List<ChangeUserStatusDto> changeUserStatusDtoList) {
        User user;
        for (ChangeUserStatusDto u : changeUserStatusDtoList
                ) {
            user = userDao.findUserByUsername(u.getUsername()).get();
            user.setUserStatus(ACTIVE);
            userDao.save(user);
        }
    }

    @Transactional
    public void changeUserStatusOnInactive(List<ChangeUserStatusDto> changeUserStatusDtoList) {
        User user;
        for (ChangeUserStatusDto u : changeUserStatusDtoList
                ) {
            user = userDao.findUserByUsername(u.getUsername()).get();
            user.setUserStatus(INACTIVE);
            userDao.save(user);
        }
    }

    public List<UserWithProfileDto> getAllUsersWithProfile() {
        return userDao.findAllUsersWithProfile();
    }

    @Transactional
    public void changeUserPassword(UserDto user) {
        User oldUser = getUserByUsername(user.getUsername()).get();
        oldUser.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.save(oldUser);
        passwordTokenService.deleteToken(user.getUsername());
    }

    public UserEmailDto getUserEmailByUsername(String username) {
        return userDao.findUsersEmailByUsername(username);
    }

    public UserBankAccountDto getUserBankAccount() throws NoBankAccountException {
        long countNumber;
        long accessKey;
        if (userUtil.getCurrentUser().getRole() == Role.COMPANY) {
            Company currentCompany = userUtil.getCurrentCompany();
            countNumber = currentCompany.getCountNumber();
            accessKey = currentCompany.getAccessKey();
        } else {
            Customer currentCustomer = userUtil.getCurrentCustomer();
            countNumber = currentCustomer.getCountNumber();
            accessKey = currentCustomer.getAccessKey();
        }
        if (countNumber == 0) {
            throw new NoBankAccountException();
        }
        return new UserBankAccountDto(countNumber, accessKey);
    }

    public void setSocketToken() {
        User currentUser = userUtil.getCurrentUser();
        currentUser.setSocketToken(passwordEncoder.encode(webSocketDistributeService.generateSocketToken()).replace('/','a'));
        userDao.save(currentUser);
    }

}
