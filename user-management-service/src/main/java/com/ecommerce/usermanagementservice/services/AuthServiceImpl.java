package com.ecommerce.usermanagementservice.services;

import com.ecommerce.usermanagementservice.Exceptions.InvalidCredentialsException;
import com.ecommerce.usermanagementservice.Exceptions.UserExistsException;
import com.ecommerce.usermanagementservice.dtos.AuthenticatedUser;
import com.ecommerce.usermanagementservice.dtos.AuthorizedUser;
import com.ecommerce.usermanagementservice.enums.MessageText;
import com.ecommerce.usermanagementservice.enums.UserState;
import com.ecommerce.usermanagementservice.models.User;
import com.ecommerce.usermanagementservice.repositories.UserRepository;
import com.ecommerce.usermanagementservice.utils.AuthUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthUtil authUtil;

    @Override
    public User signup(User inputUser) throws UserExistsException {
        var existingUser = this.userRepository.findByEmail(inputUser.getEmail());
        if(existingUser.isPresent()){
            throw new UserExistsException("User with email already exists");
        }
        var user = new User();
        user.setEmail(inputUser.getEmail());
        String hashedPassword = encodePassword(inputUser.getPassword());
        user.setPassword(hashedPassword);
        user.setName(inputUser.getName());
        user.setState(UserState.ACTIVE);
        userRepository.save(user);
        return user;
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
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
        authenticatedUser.setToken(authUtil.generateLoggedInUserToken(existintUser));
        return authenticatedUser;
    }

    @Override
    @Transactional
    public User resetPassword(AuthorizedUser user, String newPassword) {
        var userId = user.getId();
        var userToUpdate = userRepository.findById(userId).get();
        var encodedPass = this.encodePassword(newPassword);
        userToUpdate.setPassword(encodedPass);
        this.userRepository.save(userToUpdate);
        return userToUpdate;
    }
}
