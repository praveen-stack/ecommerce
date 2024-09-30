package com.ecommerce.usermanagementservice.controllers;

import com.ecommerce.usermanagementservice.Exceptions.UserExistsException;
import com.ecommerce.usermanagementservice.configuration.SecurityConfig;
import com.ecommerce.usermanagementservice.dtos.ErrorResponseDto;
import com.ecommerce.usermanagementservice.dtos.UserSignupDto;
import com.ecommerce.usermanagementservice.mappers.UserDtoMapper;
import com.ecommerce.usermanagementservice.mappers.UserSignupDtoMapper;
import com.ecommerce.usermanagementservice.models.User;
import com.ecommerce.usermanagementservice.services.AuthService;
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
@Import({SecurityConfig.class, ModelMapper.class, UserSignupDtoMapper.class, UserDtoMapper.class})
public class AutoControllerMvcTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private UserDtoMapper userDtoMapper;

    @Autowired
    private ObjectMapper objectMapper;

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

}
