package com.ecommerce.usermanagementservice.services;

import com.ecommerce.usermanagementservice.Exceptions.NotFoundException;
import com.ecommerce.usermanagementservice.Exceptions.UserExistsException;
import com.ecommerce.usermanagementservice.dtos.AuthorizedUser;
import com.ecommerce.usermanagementservice.enums.UserState;
import com.ecommerce.usermanagementservice.models.Address;
import com.ecommerce.usermanagementservice.models.User;
import com.ecommerce.usermanagementservice.repositories.AddressRepository;
import com.ecommerce.usermanagementservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

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
        userToUpdate.setPhoneNumber(userInput.getPhoneNumber());
        userToUpdate = this.userRepository.save(userToUpdate);
        userToUpdate.getAddresses();
        return userToUpdate;
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
        // fetch address lazy
        user.get().getAddresses();
        return user.get();
    }

    @Override
    @Transactional
    public Address updateAddress(AuthorizedUser authUser, Address address) {
        var user = this.getValidUserById(authUser.getId());
        address.setUser(user);
        address = this.addressRepository.save(address);
        return address;
    }

    @Override
    public List<Address> getAddresses(AuthorizedUser user) {
        return addressRepository.findByUserId(user.getId());
    }

    @Override
    @Transactional
    public void deleteAddress(AuthorizedUser user, Long addressId) {
        this.addressRepository.deleteByIdAndUserId(addressId, user.getId());
    }

    @Override
    public Address getAddressById(AuthorizedUser user, Long addressId) {
        User dbUser = getValidUserById(user.getId());
        var address = addressRepository.findById(addressId);
        if(!address.isPresent() || !address.get().getUser().getId().equals(dbUser.getId())){
            throw new NotFoundException("Address not found");
        }
        return address.get();
    }
}
