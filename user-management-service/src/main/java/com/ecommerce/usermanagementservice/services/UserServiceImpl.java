package com.ecommerce.usermanagementservice.services;

import com.ecommerce.usermanagementservice.Exceptions.UserExistsException;
import com.ecommerce.usermanagementservice.dtos.AuthorizedUser;
import com.ecommerce.usermanagementservice.enums.UserState;
import com.ecommerce.usermanagementservice.models.User;
import com.ecommerce.usermanagementservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUserByEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        var user = this.userRepository.findByEmail(email);
        if(user.isEmpty()){
            return null;
        }
        return user.get();
    }

    @Override
    @Transactional
    public User updateUser(AuthorizedUser user, User userInput) throws UserExistsException {
        var userToUpdate = this.getUserByEmail(user.getEmail());
        if(!userInput.getEmail().equals(userToUpdate.getEmail())){
            var anotherUserWithSameEmail = this.getUserByEmail(userInput.getEmail());
            if(anotherUserWithSameEmail!=null){
                throw new UserExistsException("User with email already exists");
            }
        }
        userToUpdate.setEmail(userInput.getEmail());
        userToUpdate.setName(userInput.getName());
        return this.userRepository.save(userToUpdate);
    }

    @Override
    public User getValidUserById(Long id) {
        var user = userRepository.findById(id);
        if(user==null){
            return null;
        }

        if(user.get().getState()!= UserState.ACTIVE){
            return null;
        }
        return user.get();
    }
}
