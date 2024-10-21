package com.ecommerce.usermanagementservice.components;

import com.ecommerce.usermanagementservice.dtos.OAuth2AuthUser;
import com.ecommerce.usermanagementservice.utils.AuthUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private AuthUtil authUtil;

    // This method is called when the user is successfully authenticated with OAuth2.
    // It generates a token for the user and sets it in the response header.
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthUser oAuth2User = (OAuth2AuthUser)authentication.getPrincipal();
        var token = authUtil.generateLoggedInUserToken(oAuth2User.getUser());
        response.addHeader(HttpHeaders.SET_COOKIE, authUtil.createLoginCookie(token).toString());
    }
}
