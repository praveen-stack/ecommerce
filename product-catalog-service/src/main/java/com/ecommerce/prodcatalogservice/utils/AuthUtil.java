package com.ecommerce.prodcatalogservice.utils;

import com.ecommerce.prodcatalogservice.enums.AuthConstants;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Component
public class AuthUtil {
    public String getAuthCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(cookies == null){
            return null;
        }
        var authCookie = Arrays.stream(cookies)
                .filter(cookie -> AuthConstants.AUTH_COKIE_NAME.equals(cookie.getName()))
                .findFirst()
                .orElse(null);
        if (authCookie == null) {
            return null;
        }
        return authCookie.getValue();
    }

    public String getAuthBearer(HttpServletRequest request) {
        // get from bearer token
        var header = request.getHeader("Authorization");
        if(header!=null && header.startsWith("Bearer ")){
            return header.substring(7);
        }
        return null;
    }

    public String getAuthToken(HttpServletRequest request) {
        var token = getAuthCookie(request);
        if(token!=null){
            return token;
        }
        token = getAuthBearer(request);
        return token;
    }

    public ResponseCookie createLoginCookie(String token) {
        return ResponseCookie.from(AuthConstants.AUTH_COKIE_NAME, token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(24 * 60 * 60) // 1 day in seconds
                .build();
    }
}
