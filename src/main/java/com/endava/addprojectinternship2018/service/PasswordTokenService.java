package com.endava.addprojectinternship2018.service;

import com.endava.addprojectinternship2018.dao.PasswordTokenDao;
import com.endava.addprojectinternship2018.model.PasswordToken;
import com.endava.addprojectinternship2018.model.dto.UserWithProfileDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class PasswordTokenService {

    @Autowired
    private PasswordTokenDao passwordTokenDao;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private EmailServiceImpl emailService;

    @Autowired
    private UserService userService;

    public Optional<PasswordToken> getPasswordTokenByUsername(String username){
        return passwordTokenDao.findByUsername(username);
    }

    public void save(PasswordToken passwordToken){
        Optional<PasswordToken> oldPasswordToken = getPasswordTokenByUsername(passwordToken.getUsername());
        if (userService.getUserByUsername(passwordToken.getUsername()).isPresent()){
            userService.changeUserStatus(passwordToken.getUsername());
            if (oldPasswordToken.isPresent()){
                oldPasswordToken.get().setUsername(passwordToken.getUsername());
                oldPasswordToken.get().setToken(passwordEncoder.encode(passwordToken.getToken()));
                passwordTokenDao.save(oldPasswordToken.get());
            } else {
                passwordToken.setToken(passwordEncoder.encode(passwordToken.getToken()));
                passwordTokenDao.save(passwordToken);
//            UserEmailDto usersEmailDto = userService.getUsersEmailByUsername(passwordToken.getUsername());
                UserWithProfileDto userWithProfileDto = userService.getAllUsersWithProfile().stream().filter(u -> u.getUsername().equals(passwordToken.getUsername())).findFirst().get();
                String RESET_PASS_LINK = "http://localhost:8080/reset/password?username=" + passwordToken.getUsername() + "&token=" + passwordToken.getToken();
                emailService.sendSimpleMessage(userWithProfileDto.getEmail(), "Endava reset password!", RESET_PASS_LINK);
            }
        }
    }

    public boolean isTokenActive(String username, String token) {
        Optional<PasswordToken> oldPasswordToken = passwordTokenDao.findByUsername(username);
        if (oldPasswordToken.isPresent()){
            if (oldPasswordToken.get().getUsername().equals(username) && token.equals(oldPasswordToken.get().getToken())){
                return true;
            }
        }
        return false;
    }

    @Transactional
    public void deleteToken(String username) {
        passwordTokenDao.deleteByUsername(username);
    }


}
