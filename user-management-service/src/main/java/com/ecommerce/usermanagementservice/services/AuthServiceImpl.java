package com.ecommerce.usermanagementservice.services;

import com.ecommerce.usermanagementservice.Exceptions.InvalidCredentialsException;
import com.ecommerce.usermanagementservice.Exceptions.UserExistsException;
import com.ecommerce.usermanagementservice.dtos.AuthenticatedUser;
import com.ecommerce.usermanagementservice.enums.MessageText;
import com.ecommerce.usermanagementservice.models.User;
import com.ecommerce.usermanagementservice.repositories.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SecretKey secretKey;

    @Override
    public User signup(User inputUser) throws UserExistsException {
        var existingUser = this.userRepository.findByEmail(inputUser.getEmail());
        if(existingUser.isPresent()){
            throw new UserExistsException("User with email already exists");
        }
        var user = new User();
        user.setEmail(inputUser.getEmail());
        String hashedPassword = passwordEncoder.encode(inputUser.getPassword());
        user.setPassword(hashedPassword);
        user.setName(inputUser.getName());
        userRepository.save(user);
        return user;
    }


    private String generateLoggedInUserToken(User user){
        Map<String, String> claims = new HashMap<>();
        claims.put("sub", user.getId().toString());
        claims.put("name", user.getName());
        claims.put("email", user.getEmail());
        var currentTimeInMillis = System.currentTimeMillis();
        // 24 hour expiry
        var expiryInMillis = currentTimeInMillis + 24 * 60 * 60 * 1000;
        claims.put("exp", Long.toString(expiryInMillis));
        claims.put("iat", Long.toString(currentTimeInMillis));
        return Jwts.builder().claims(claims).signWith(secretKey, SignatureAlgorithm.HS256).compact();
    }

    @Override
    public AuthenticatedUser login(String email, String password) throws InvalidCredentialsException {
        var existingUserOptional = this.userRepository.findByEmail(email);
        if(!existingUserOptional.isPresent()){
            throw new InvalidCredentialsException(MessageText.INCORRECT_EMAIL_PASSWORD.getValue());
        }
        String hashedPassword = passwordEncoder.encode(password);
        var existintUser = existingUserOptional.get();
        if(!passwordEncoder.matches(password, existintUser.getPassword())){
            throw new InvalidCredentialsException(MessageText.INCORRECT_EMAIL_PASSWORD.getValue());
        }
        var authenticatedUser = new AuthenticatedUser();
        authenticatedUser.setUser(existintUser);
        authenticatedUser.setToken(this.generateLoggedInUserToken(existintUser));
        return authenticatedUser;
    }
}
