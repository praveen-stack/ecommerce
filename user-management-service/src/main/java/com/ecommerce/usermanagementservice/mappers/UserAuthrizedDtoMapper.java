package com.ecommerce.usermanagementservice.mappers;

import com.ecommerce.usermanagementservice.components.ObjectMapper;
import com.ecommerce.usermanagementservice.dtos.AuthorizedUser;
import com.ecommerce.usermanagementservice.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserAuthrizedDtoMapper  extends ObjectMapper<AuthorizedUser, User> {
    @Override
    public Class<AuthorizedUser> getDtoClass() {
        return AuthorizedUser.class;
    }

    @Override
    public Class<User> getEntityClass() {
        return User.class;
    }
}
