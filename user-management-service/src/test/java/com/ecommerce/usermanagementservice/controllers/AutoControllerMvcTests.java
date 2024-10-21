package com.ecommerce.usermanagementservice.controllers;

import com.ecommerce.usermanagementservice.Exceptions.InvalidCredentialsException;
import com.ecommerce.usermanagementservice.Exceptions.UserExistsException;
import com.ecommerce.usermanagementservice.components.AutherisationFilter;
import com.ecommerce.usermanagementservice.configuration.AppConfig;
import com.ecommerce.usermanagementservice.configuration.SecurityConfig;
import com.ecommerce.usermanagementservice.dtos.AuthenticatedUser;
import com.ecommerce.usermanagementservice.dtos.ErrorResponseDto;
import com.ecommerce.usermanagementservice.dtos.UserLoginDto;
import com.ecommerce.usermanagementservice.dtos.UserSignupDto;
import com.ecommerce.usermanagementservice.enums.AuthConstants;
import com.ecommerce.usermanagementservice.mappers.UserAuthrizedDtoMapper;
import com.ecommerce.usermanagementservice.mappers.UserDtoMapper;
import com.ecommerce.usermanagementservice.mappers.UserSignupDtoMapper;
import com.ecommerce.usermanagementservice.models.User;
import com.ecommerce.usermanagementservice.services.AuthService;
import com.ecommerce.usermanagementservice.services.UserServiceImpl;
import com.ecommerce.usermanagementservice.utils.AuthUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;
import java.util.HashMap;
import java.util.Map;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import({SecurityConfig.class, UserSignupDtoMapper.class, UserDtoMapper.class, AutherisationFilter.class, AppConfig.class, AuthUtil.class})
public class AutoControllerMvcTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private UserDtoMapper userDtoMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private UserAuthrizedDtoMapper userAuthrizedDtoMapper;

    @Test
    public void Test_Signup_Validations() throws Exception {
        UserSignupDto dto = new UserSignupDto();
        Map<String, String> validationResponse = new HashMap<>();
        validationResponse.put("name", "must not be blank");
        validationResponse.put("password", "must not be blank");
        validationResponse.put("email", "must not be blank");

        mockMvc.perform(post("/auth/signup")
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(objectMapper.writeValueAsString(validationResponse)));
    }

    @Test
    public void Test_Signup_Email_Validations() throws Exception {
        UserSignupDto dto = new UserSignupDto();
        dto.setName("abc");
        dto.setPassword("abcD#223fd3d");
        dto.setEmail("avc");
        Map<String, String> validationResponse = new HashMap<>();
        validationResponse.put("email", "must be a well-formed email address");
        mockMvc.perform(post("/auth/signup")
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(objectMapper.writeValueAsString(validationResponse)));
    }

    @Test
    public void Test_Signup_Success() throws Exception {
        UserSignupDto dto = new UserSignupDto();
        dto.setName("abc");
        dto.setPassword("abcD#223fd3d");
        dto.setEmail("avc@mockmail.com");
        User userResponse = new User();
        userResponse.setPassword(dto.getPassword());
        userResponse.setName(dto.getName());
        userResponse.setId(1L);
        userResponse.setEmail(dto.getEmail());
        when(authService.signup(any(User.class))).thenReturn(userResponse);

        mockMvc.perform(post("/auth/signup")
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(userDtoMapper.toDto(userResponse))));
    }

    @Test
    public void Test_Signup_UserExists() throws Exception {
        UserSignupDto dto = new UserSignupDto();
        dto.setName("abc");
        dto.setPassword("abcD#223fd3d");
        dto.setEmail("avc@mockmail.com");
        when(authService.signup(any(User.class))).thenThrow(new UserExistsException("user exists"));
        ErrorResponseDto errorDto = new ErrorResponseDto();
        errorDto.setMessage("user exists");
        mockMvc.perform(post("/auth/signup")
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(content().string(objectMapper.writeValueAsString(errorDto)));
    }

    @Test
    public void Test_Login_Success() throws Exception {
        var dto = new UserLoginDto();
        dto.setPassword("abcD#223fd3d");
        dto.setEmail("avc@mockmail.com");
        User userResponse = new User();
        userResponse.setPassword(dto.getPassword());
        userResponse.setName("name");
        userResponse.setId(1L);
        userResponse.setEmail(dto.getEmail());
        AuthenticatedUser authUser = new AuthenticatedUser();
        authUser.setToken("token");
        authUser.setUser(userResponse);
        when(authService.login(any(String.class), any(String.class))).thenReturn(authUser);

        mockMvc.perform(post("/auth/login")
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(userDtoMapper.toDto(userResponse))))
                .andExpect(header().string("Set-Cookie", org.hamcrest.Matchers.isA(String.class)))
                .andExpect(header().string("Set-Cookie", org.hamcrest.Matchers.containsString(AuthConstants.AUTH_COKIE_NAME)));
    }

    @Test
    public void Test_Login_Failure() throws Exception {
        var dto = new UserLoginDto();
        dto.setPassword("abcD#223fd3d");
        dto.setEmail("avc@mockmail.com");
        when(authService.login(any(String.class), any(String.class))).thenThrow(new InvalidCredentialsException("Invalid cred"));
        ErrorResponseDto errorDto = new ErrorResponseDto();
        errorDto.setMessage("Invalid cred");
        mockMvc.perform(post("/auth/login")
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(objectMapper.writeValueAsString(errorDto)));
    }

}
