package com.ecommerce.usermanagementservice.controllers;


import com.ecommerce.usermanagementservice.components.AutherisationFilter;
import com.ecommerce.usermanagementservice.components.OAuth2SuccessHandler;
import com.ecommerce.usermanagementservice.configuration.AppConfig;
import com.ecommerce.usermanagementservice.configuration.OAuth2Config;
import com.ecommerce.usermanagementservice.configuration.SecurityConfig;
import com.ecommerce.usermanagementservice.dtos.AuthorizedUser;
import com.ecommerce.usermanagementservice.dtos.UserSignupDto;
import com.ecommerce.usermanagementservice.enums.AuthConstants;
import com.ecommerce.usermanagementservice.mappers.UserAuthrizedDtoMapper;
import com.ecommerce.usermanagementservice.mappers.UserDtoMapper;
import com.ecommerce.usermanagementservice.mappers.UserProfileUpdateDtoMapper;
import com.ecommerce.usermanagementservice.models.User;
import com.ecommerce.usermanagementservice.services.AuthService;
import com.ecommerce.usermanagementservice.services.UserService;
import com.ecommerce.usermanagementservice.utils.AuthUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserProfileController.class)
@Import({
        SecurityConfig.class,
        UserDtoMapper.class,
        UserAuthrizedDtoMapper.class,
        AutherisationFilter.class,
        AppConfig.class,
        AuthUtil.class,
        UserProfileUpdateDtoMapper.class,
        OAuth2SuccessHandler.class,
        OAuth2Config.class
})
public class UserProfileMvcTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserDtoMapper userDtoMapper;

    @Autowired
    private AuthUtil authUtil;

    @Test
    public void Test_Reset_Password() throws Exception {
        UserSignupDto dto = new UserSignupDto();
        dto.setName("abc");
        dto.setPassword("abcD#223fd3d");
        dto.setEmail("avc@mockmail.com");
        User userResponse = new User();
        userResponse.setPassword(dto.getPassword());
        userResponse.setName(dto.getName());
        userResponse.setId(1L);
        userResponse.setEmail(dto.getEmail());
        when(authService.resetPassword(any(AuthorizedUser.class), any(String.class))).thenReturn(userResponse);
        when(userService.getValidUserById(any(Long.class))).thenReturn(userResponse);
        var token = authUtil.generateLoggedInUserToken(userResponse);
        var cookie = new Cookie(AuthConstants.AUTH_COKIE_NAME, token);
        mockMvc.perform(put("/me/reset-password")
                        .content(objectMapper.writeValueAsString(dto))
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(userDtoMapper.toDto(userResponse))));
    }

    @Test
    public void Test_Reset_Password_Unauthorized() throws Exception {
        UserSignupDto dto = new UserSignupDto();
        dto.setName("abc");
        dto.setPassword("abcD#223fd3d");
        dto.setEmail("avc@mockmail.com");
        User userResponse = new User();
        userResponse.setPassword(dto.getPassword());
        userResponse.setName(dto.getName());
        userResponse.setId(1L);
        userResponse.setEmail(dto.getEmail());
        when(authService.resetPassword(any(AuthorizedUser.class), any(String.class))).thenReturn(userResponse);
        when(userService.getValidUserById(any(Long.class))).thenReturn(userResponse);
        mockMvc.perform(put("/me/reset-password")
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
