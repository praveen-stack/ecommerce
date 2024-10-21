package com.ecommerce.usermanagementservice.dtos;

import com.ecommerce.usermanagementservice.models.User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;
import java.util.Map;

public class OAuth2AuthUser implements OAuth2User {

    private final Collection<? extends GrantedAuthority> authorities;
    private final Map<String, Object> attributes;
    private final User user;

    public OAuth2AuthUser(Collection<? extends GrantedAuthority> authorities,
                          Map<String, Object> attributes,
                          User user) {
        this.authorities = authorities;
        this.attributes = attributes;
        this.user = user;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return this.user.getEmail();  // Set email as the principal name
    }

    public User getUser() {
        return this.user;  // Expose the id for easier access
    }
}
