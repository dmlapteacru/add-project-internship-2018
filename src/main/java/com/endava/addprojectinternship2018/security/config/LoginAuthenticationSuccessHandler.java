package com.endava.addprojectinternship2018.security.config;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;


public class LoginAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final String DEFAULT_URL_ADMIN = "/admin";
    private static final String DEFAULT_URL_COMPANY = "/company/home";
    private static final String DEFAULT_URL_CUSTOMER = "/customer/home";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        Set<String> setOfAuthorities = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (setOfAuthorities.contains("ADMIN")) {
            getRedirectStrategy().sendRedirect(request, response, DEFAULT_URL_ADMIN);
        } else if (setOfAuthorities.contains("COMPANY")) {
            getRedirectStrategy().sendRedirect(request, response, DEFAULT_URL_COMPANY);
        } else
            getRedirectStrategy().sendRedirect(request, response, DEFAULT_URL_CUSTOMER);
    }


    public String authenticatedRedirectDefaultPage(Authentication authentication) {
        Set<String> setOfAuthorities = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (setOfAuthorities.contains("ADMIN")) {
            return DEFAULT_URL_ADMIN;
        } else if (setOfAuthorities.contains("COMPANY")) {
            return DEFAULT_URL_COMPANY;
        } else
            return DEFAULT_URL_CUSTOMER;
    }
}
