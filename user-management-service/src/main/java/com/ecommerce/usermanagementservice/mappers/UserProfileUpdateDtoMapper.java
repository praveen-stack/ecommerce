package com.ecommerce.usermanagementservice.mappers;

import com.ecommerce.usermanagementservice.components.ObjectMapper;
import com.ecommerce.usermanagementservice.dtos.UserProfileUpdateDto;
import com.ecommerce.usermanagementservice.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserProfileUpdateDtoMapper extends ObjectMapper<UserProfileUpdateDto, User> {
    @Override
    public Class<UserProfileUpdateDto> getDtoClass() {
        return UserProfileUpdateDto.class;
    }

    @Override
    public Class<User> getEntityClass() {
        return User.class;
    }
}
