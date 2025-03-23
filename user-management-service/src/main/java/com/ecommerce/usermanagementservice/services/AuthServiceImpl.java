package com.ecommerce.usermanagementservice.services;

import com.ecommerce.usermanagementservice.Exceptions.InvalidCredentialsException;
import com.ecommerce.usermanagementservice.Exceptions.UnauthorizedException;
import com.ecommerce.usermanagementservice.Exceptions.UserExistsException;
import com.ecommerce.usermanagementservice.dtos.AuthenticatedUser;
import com.ecommerce.usermanagementservice.dtos.AuthorizedUser;
import com.ecommerce.usermanagementservice.enums.MessageText;
import com.ecommerce.usermanagementservice.enums.UserState;
import com.ecommerce.usermanagementservice.models.PasswordResetToken;
import com.ecommerce.usermanagementservice.models.User;
import com.ecommerce.usermanagementservice.repositories.PasswordResetTokenRepository;
import com.ecommerce.usermanagementservice.repositories.UserRepository;
import com.ecommerce.usermanagementservice.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private EmailService emailService;

    @Override
    public User signup(User inputUser) throws UserExistsException {
        var existingUser = this.userRepository.findByEmail(inputUser.getEmail());
        if(existingUser.isPresent()){
            throw new UserExistsException("User with email already exists");
        }
        var user = new User();
        user.setEmail(inputUser.getEmail());
        String hashedPassword = null;
        if (inputUser.getPassword() != null){
            hashedPassword = encodePassword(inputUser.getPassword());
        }
        user.setPassword(hashedPassword);
        user.setName(inputUser.getName());
        user.setPhoneNumber(inputUser.getPhoneNumber());
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
        var existintUser = existingUserOptional.get();
        if(!passwordEncoder.matches(password, existintUser.getPassword())){
            throw new InvalidCredentialsException(MessageText.INCORRECT_EMAIL_PASSWORD.getValue());
        }
        var authenticatedUser = new AuthenticatedUser();
        authenticatedUser.setUser(existintUser);
        authenticatedUser.setToken(authUtil.generateLoggedInUserToken(existintUser));
        return authenticatedUser;
    }

    private User resetPassword(String newPassword, User user) {
        var encodedPass = this.encodePassword(newPassword);
        user.setPassword(encodedPass);
        this.userRepository.save(user);
        return user;
    }

    @Override
    @Transactional
    public User resetPassword(AuthorizedUser user, String newPassword) {
        var userId = user.getId();
        var userToUpdate = userRepository.findById(userId).get();
        return resetPassword(newPassword, userToUpdate);
    }

    @Override
    @Transactional
    public User resetPassword(String resetToken, String newPassword) {
        var token = passwordResetTokenRepository.findByToken(resetToken);
        if(token == null ||  token.getExpiryDate().before(new Date())){
            throw new UnauthorizedException("Invalid reset token");
        }
        var user = token.getUser();
        user = this.resetPassword(newPassword, user);
        passwordResetTokenRepository.delete(token);
        return user;
    }

    @Transactional
    public void generatePasswordResetToken(String email) {
        var userOptional = this.userRepository.findByEmail(email);
        if(!userOptional.isPresent()){
            throw new UnauthorizedException("Invalid account");
        }
        var user = userOptional.get();
        PasswordResetToken tokenObj = this.passwordResetTokenRepository.findByUserId(user.getId());
        if(tokenObj == null){
            tokenObj = new PasswordResetToken();
        }
        var token = UUID.randomUUID().toString();
        // expire after 1 hour
        var expiryDate = new Date(System.currentTimeMillis() + 3600000);
        tokenObj.setToken(token);
        tokenObj.setExpiryDate(expiryDate);
        tokenObj.setUser(user);
        passwordResetTokenRepository.save(tokenObj);
        
        // Send password reset email
        emailService.sendPasswordResetEmail(email, token);
    }
}
