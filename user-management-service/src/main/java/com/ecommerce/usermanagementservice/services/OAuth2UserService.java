package com.ecommerce.usermanagementservice.services;

import com.ecommerce.usermanagementservice.Exceptions.UserExistsException;
import com.ecommerce.usermanagementservice.dtos.OAuth2AuthUser;
import com.ecommerce.usermanagementservice.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class OAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Override
    public OAuth2AuthUser loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        User user = userService.getUserByEmail(email);
        if(user == null){
            user = new User();
            user.setEmail(email);
            user.setName(name);
            try {
                user = authService.signup(user);
            } catch (UserExistsException e) {
                throw new RuntimeException("Error while signing up user using google oauth2");
            }
        }
        return new OAuth2AuthUser(oAuth2User.getAuthorities(), attributes, user);
    }
}
