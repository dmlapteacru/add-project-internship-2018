package com.endava.addprojectinternship2018.security.config;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response, AuthenticationException exception) throws IOException{

        if (exception.getMessage().equals("USER_NOT_FOUND")) {
            getRedirectStrategy().sendRedirect(request, response, "/login?error=no_user");
        } else if (exception.getMessage().equals("USER_IS_INACTIVE")){
            getRedirectStrategy().sendRedirect(request, response, "/login?error=inactive_user");
        } else {
            getRedirectStrategy().sendRedirect(request, response, "/login?error=wrong_password");
        }
    }
}
