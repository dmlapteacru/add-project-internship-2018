package com.endava.addprojectinternship2018.service.userDetailsService;

import com.endava.addprojectinternship2018.model.User;
import com.endava.addprojectinternship2018.model.enums.UserStatus;
import com.endava.addprojectinternship2018.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final String NO_SUCH_USER = "USER_NOT_FOUND";
    private static final String INACTIVE_USER = "USER_IS_INACTIVE";

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws BadCredentialsException {
        if (!userService.getUserByUsername(username).isPresent()){
            throw new BadCredentialsException(NO_SUCH_USER);
        }
        if (userService.getUserByUsername(username).get().getUserStatus() != UserStatus.ACTIVE){
            throw new BadCredentialsException(INACTIVE_USER);
        }
        User user = userService.getUserByUsername(username).get();
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole().toString()));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }
}
