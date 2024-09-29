package com.ecommerce.usermanagementservice.mappers;

import com.ecommerce.usermanagementservice.components.ObjectMapper;
import com.ecommerce.usermanagementservice.dtos.UserSignupDto;
import com.ecommerce.usermanagementservice.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserSignupDtoMapper extends ObjectMapper<UserSignupDto, User> {
    @Override
    public Class getDtoClass() {
        return UserSignupDto.class;
    }

    @Override
    public Class getEntityClass() {
        return User.class;
    }
}