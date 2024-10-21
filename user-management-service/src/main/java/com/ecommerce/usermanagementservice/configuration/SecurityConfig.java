package com.ecommerce.usermanagementservice.configuration;

import com.ecommerce.usermanagementservice.components.AutherisationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private AutherisationFilter autherisationFilter;

    @Bean
    public BCryptPasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private AuthenticationSuccessHandler oAuth2SuccessHandler;

    @Bean
    public SecurityFilterChain getSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors().disable();
        httpSecurity.csrf().disable();

        httpSecurity.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/auth/**", "/auth/signup").permitAll()
                .anyRequest().authenticated());
        httpSecurity.oauth2Login().successHandler(oAuth2SuccessHandler);
        httpSecurity.addFilterBefore(autherisationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}
