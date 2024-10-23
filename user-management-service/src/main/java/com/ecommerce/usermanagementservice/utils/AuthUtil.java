package com.ecommerce.usermanagementservice.utils;

import com.ecommerce.usermanagementservice.dtos.AuthorizedUser;
import com.ecommerce.usermanagementservice.enums.AuthConstants;
import com.ecommerce.usermanagementservice.mappers.UserAuthrizedDtoMapper;
import com.ecommerce.usermanagementservice.models.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthUtil {
    @Autowired
    private SecretKey secretKey;

    @Autowired
    private UserAuthrizedDtoMapper userAuthrizedDtoMapper;

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

    public String getAuthToken(HttpServletRequest request) {
        return getAuthCookie(request);
    }

    public UsernamePasswordAuthenticationToken getAuthentication(User userDetails) {
        AuthorizedUser userAuthrizedDto = this.userAuthrizedDtoMapper.toDto(userDetails);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userAuthrizedDto, null, null);
        return authentication;
    }

    public String generateLoggedInUserToken(User user){
        Map<String, String> claims = new HashMap<>();
        claims.put("sub", user.getId().toString());
        claims.put("name", user.getName());
        claims.put("email", user.getEmail());
        var currentTimeInMillis = System.currentTimeMillis();
        long expMillis = currentTimeInMillis + 3600000 * 24;
        return Jwts.builder().issuedAt(new Date()).expiration(new Date(expMillis)).claims(claims).signWith(secretKey, SignatureAlgorithm.HS256).compact();
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
