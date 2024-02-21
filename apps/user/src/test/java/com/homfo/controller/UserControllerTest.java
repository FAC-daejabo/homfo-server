package com.homfo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.homfo.auth.command.TokenRefreshCommand;
import com.homfo.auth.dto.JwtDto;
import com.homfo.auth.entity.CustomUserDetails;
import com.homfo.config.TestSecurityConfig;
import com.homfo.enums.Gender;
import com.homfo.enums.MarketingCode;
import com.homfo.user.command.RegisterCommand;
import com.homfo.user.command.SignInCommand;
import com.homfo.user.dto.MarketingAgreementDto;
import com.homfo.user.dto.UserDto;
import com.homfo.user.dto.UserMarketingAgreementDto;
import com.homfo.user.infra.enums.UserStatus;
import com.homfo.user.service.UserRefreshTokenWriteService;
import com.homfo.user.service.UserWriteService;
import com.homfo.user.usecase.GetUserInfoUsecase;
import com.homfo.user.usecase.RegisterUsecase;
import com.homfo.user.usecase.SignInUsecase;
import com.homfo.user.usecase.TokenRefreshUsecase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UserController.class)
@WebMvcTest(UserController.class)
@Import({TestSecurityConfig.class})
class UserControllerTest {
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private UserWriteService userWriteService;

    @MockBean
    private UserRefreshTokenWriteService userRefreshTokenWriteService;

    @MockBean
    private GetUserInfoUsecase getUserInfoUsecase;

    @MockBean
    private RegisterUsecase registerUsecase;

    @MockBean
    private SignInUsecase signInUsecase;

    @MockBean
    private TokenRefreshUsecase tokenRefreshUsecase;

    @BeforeEach
    void setup() {
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());

        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    @WithMockUser
    void signIn_ShouldReturnJwtToken() throws Exception {
        // given
        SignInCommand command = new SignInCommand("testAccount", "testPW@111");
        JwtDto jwtDto = new JwtDto("token", "refreshToken");
        given(signInUsecase.execute(command)).willReturn(jwtDto);

        // when & then
        mockMvc.perform(post("/users/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(jwtDto)));
    }

    // info 메소드 테스트
    @Test
    @WithMockUser
    void info_ShouldReturnUserMarketingAgreementDto() throws Exception {
        // given
        Long userId = 1L; // 예시 ID
        CustomUserDetails customUserDetails = new CustomUserDetails(userId);
        UserDto userDto = new UserDto(userId, "testUser", "testUser", "999-9999-9999", Gender.MAN, "job", LocalDate.now(), UserStatus.USE);
        List<MarketingAgreementDto> marketingAgreementDtoList = new ArrayList<>(List.of(new MarketingAgreementDto(MarketingCode.SEND_INFORMATION_TO_THIRD_PARTY, true)));
        UserMarketingAgreementDto userMarketingAgreementDto = new UserMarketingAgreementDto(userDto, marketingAgreementDtoList);

        given(getUserInfoUsecase.execute(userId)).willReturn(userMarketingAgreementDto);

        // when & then
        mockMvc.perform(get("/users/info").with(user(customUserDetails)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userMarketingAgreementDto)));
    }

    // register 메소드 테스트
    @Test
    @WithMockUser
    void register_ShouldReturnJwtDto() throws Exception {
        // given
        String account = "testAccount";
        String password = "testPW@111";
        String nickname = "닉네임";
        String phoneNumber = "010-1234-1234";
        Gender gender = Gender.MAN; // Gender enum이 M,F 등으로 정의되었다고 가정
        String job = "학생";
        LocalDate birthday = LocalDate.of(2000, 12, 12);
        List<MarketingAgreementDto> marketingCodeList = List.of(new MarketingAgreementDto(MarketingCode.SEND_INFORMATION_TO_THIRD_PARTY, true));
        RegisterCommand command = new RegisterCommand(account, password, nickname, phoneNumber, gender, job, birthday, marketingCodeList);
        JwtDto jwtDto = new JwtDto("token", "refreshToken");

        given(registerUsecase.execute(command)).willReturn(jwtDto);

        // when & then
        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(jwtDto)));
    }

    // signOut 메소드 테스트
    @Test
    @WithMockUser
    void signOut_ShouldReturnOk() throws Exception {
        // given
        Long userId = 1L;
        CustomUserDetails customUserDetails = new CustomUserDetails(userId);

        // when & then
        mockMvc.perform(patch("/users/sign-out").with(user(customUserDetails)))
                .andExpect(status().isOk());
    }

    // deleteAccount 메소드 테스트
    @Test
    @WithMockUser
    void deleteAccount_ShouldReturnOk() throws Exception {
        // given
        Long userId = 1L;
        CustomUserDetails customUserDetails = new CustomUserDetails(userId);

        // when & then
        mockMvc.perform(delete("/users/account").with(user(customUserDetails)))
                .andExpect(status().isOk());
    }

    // refreshToken 메소드 테스트
    @Test
    @WithMockUser
    void refreshToken_ShouldReturnJwtDto() throws Exception {
        // given
        TokenRefreshCommand command = new TokenRefreshCommand("refreshToken");
        JwtDto jwtDto = new JwtDto("newToken", "newRefreshToken");
        Long userId = 1L;
        CustomUserDetails customUserDetails = new CustomUserDetails(userId);
        given(tokenRefreshUsecase.execute(userId, command)).willReturn(jwtDto);

        // when & then
        mockMvc.perform(patch("/users/refresh")
                        .with(user(customUserDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(jwtDto)));
    }

}
