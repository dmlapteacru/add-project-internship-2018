package com.endava.addprojectinternship2018.service.user.detailsService;

import com.endava.addprojectinternship2018.model.User;
import com.endava.addprojectinternship2018.model.UserStatus;
import com.endava.addprojectinternship2018.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!userService.getUserByUsername(username).isPresent()){
            throw new UsernameNotFoundException("No such user");
        }
        if (userService.getUserByUsername(username).get().getUserStatus() != UserStatus.ACTIVE){
            throw new UsernameNotFoundException("user is inactive");
        }
        User user = userService.getUserByUsername(username).get();
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole().toString()));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }
}
