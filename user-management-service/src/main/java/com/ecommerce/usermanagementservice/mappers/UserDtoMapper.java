package com.ecommerce.usermanagementservice.mappers;

import com.ecommerce.usermanagementservice.components.ObjectMapper;
import com.ecommerce.usermanagementservice.dtos.UserDto;
import com.ecommerce.usermanagementservice.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserDtoMapper extends ObjectMapper<UserDto, User> {
    @Override
    public Class getDtoClass() {
        return UserDto.class;
    }

    @Override
    public Class getEntityClass() {
        return User.class;
    }
}
