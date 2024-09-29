package com.ecommerce.usermanagementservice.services;

import com.ecommerce.usermanagementservice.Exceptions.UserExistsException;
import com.ecommerce.usermanagementservice.models.User;
import com.ecommerce.usermanagementservice.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @Override
    public User signup(User inputUser) {
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
}
