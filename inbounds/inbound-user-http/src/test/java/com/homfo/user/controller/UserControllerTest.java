package com.homfo.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.homfo.auth.command.TokenRefreshCommand;
import com.homfo.auth.dto.JwtDto;
import com.homfo.auth.entity.CustomUserDetails;
import com.homfo.enums.Gender;
import com.homfo.enums.MarketingCode;
import com.homfo.error.BadRequestException;
import com.homfo.error.CommonErrorCode;
import com.homfo.error.ResourceNotFoundException;
import com.homfo.user.command.RegisterCommand;
import com.homfo.user.command.SignInCommand;
import com.homfo.user.config.TestSecurityConfig;
import com.homfo.user.dto.MarketingAgreementDto;
import com.homfo.user.dto.UserDto;
import com.homfo.user.dto.UserMarketingAgreementDto;
import com.homfo.user.infra.enums.UserErrorCode;
import com.homfo.user.infra.enums.UserStatus;
import com.homfo.user.request.*;
import com.homfo.user.response.JwtResponse;
import com.homfo.user.response.UserMarketingAgreementResponse;
import com.homfo.user.usecase.GetUserInfoUsecase;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UserController.class)
@WebMvcTest(UserController.class)
@Import({TestSecurityConfig.class, TestUserControllerConfig.class})
class UserControllerTest {
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private MockUserManagingUsecase manageUserService;

    @MockBean
    private MockUserValidationUsecase validateUserService;

    @MockBean
    private GetUserInfoUsecase getUserInfoUsecase;

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
    @DisplayName("로그인 성공")
    void signIn_ShouldReturnJwtToken() throws Exception {
        // given
        SignInRequest request = new SignInRequest("testAccount", "TestPW@111");
        SignInCommand command = new SignInCommand("testAccount", "TestPW@111");
        JwtDto jwtDto = new JwtDto("token", "refreshToken");
        JwtResponse response = new JwtResponse(jwtDto);
        given(manageUserService.signIn(command)).willReturn(jwtDto);

        // when & then
        mockMvc.perform(post("/users/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    @DisplayName("로그인 실패")
    void signIn_Throws_ServletException() {
        // given
        SignInRequest request = new SignInRequest("testAccount", "TestPW@111");
        SignInCommand command = new SignInCommand("testAccount", "TestPW@111");

        given(manageUserService.signIn(command)).willThrow(new ResourceNotFoundException(UserErrorCode.NOT_EXIST_USER));

        // when & then
        // RestControllerAdvice 같은 설정이 따로 없어서 커스텀된 에러는 ServletException를 반환
        assertThrows(ServletException.class, () -> mockMvc.perform(post("/users/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))));
    }

    @Test
    @DisplayName("정규식에 맞지 않는 계정으로 로그인 시도")
    void signIn_BadRequest_NotValidAccount() throws Exception {
        // given
        String givenAccount = "testAccount!@#@!#";
        String givenPassword = "TestPW@111";
        SignInRequest request = new SignInRequest(givenAccount, givenPassword);

        // when & then
        mockMvc.perform(post("/users/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    @DisplayName("정규식에 맞지 않는 비밀번호로 로그인 시도")
    void signIn_BadRequest_NotValidPassword() throws Exception {
        // given
        String givenAccount = "testAccount";
        String givenPassword = "TestPW111";
        SignInRequest request = new SignInRequest(givenAccount, givenPassword);

        // when & then
        mockMvc.perform(post("/users/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    @WithMockUser
    @DisplayName("자기 자신의 사용자 정보 조회")
    void info_ShouldReturnUserMarketingAgreementDto() throws Exception {
        // given
        Long userId = 1L;
        CustomUserDetails customUserDetails = new CustomUserDetails(userId);
        UserDto userDto = new UserDto(userId, "testUser", "testUser", "999-9999-9999", Gender.MAN, "job", LocalDate.now(), UserStatus.USE);
        List<MarketingAgreementDto> marketingAgreementDtoList = new ArrayList<>(List.of(new MarketingAgreementDto(MarketingCode.SEND_INFORMATION_TO_THIRD_PARTY, true)));
        UserMarketingAgreementDto dto = new UserMarketingAgreementDto(userDto, marketingAgreementDtoList);
        UserMarketingAgreementResponse response = new UserMarketingAgreementResponse(dto);

        given(getUserInfoUsecase.getUserInfo(userId)).willReturn(dto);

        // when & then
        mockMvc.perform(get("/users/info").with(user(customUserDetails)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    @WithMockUser
    @DisplayName("회원가입 성공")
    void register_ShouldReturnJwtDto() throws Exception {
        // given
        String account = "testAccount";
        String password = "testPW@111";
        String nickname = "닉네임";
        String phoneNumber = "010-1234-1234";
        Gender gender = Gender.MAN;
        String job = "학생";
        LocalDate birthday = LocalDate.of(2000, 12, 12);
        List<MarketingAgreementDto> marketingCodeList = List.of(new MarketingAgreementDto(MarketingCode.SEND_INFORMATION_TO_THIRD_PARTY, true));
        List<MarketingAgreementRequest> marketingCodeRequsetList = List.of(new MarketingAgreementRequest(MarketingCode.SEND_INFORMATION_TO_THIRD_PARTY, true));
        RegisterRequest request = new RegisterRequest(account, password, nickname, phoneNumber, gender, job, birthday, marketingCodeRequsetList);
        RegisterCommand command = new RegisterCommand(account, password, nickname, phoneNumber, gender, job, birthday, marketingCodeList);
        JwtDto jwtDto = new JwtDto("token", "refreshToken");
        JwtResponse response = new JwtResponse(jwtDto);

        given(manageUserService.register(command)).willReturn(jwtDto);

        // when & then
        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    @WithMockUser
    @DisplayName("잘못된 데이터이거나 인증 코드가 없어서 회원가입 실패")
    void register_Throws_BadRequestException()  {
        // given
        String account = "testAccount";
        String password = "testPW@111";
        String nickname = "닉네임";
        String phoneNumber = "010-1234-1234";
        Gender gender = Gender.MAN; // Gender enum이 M,F 등으로 정의되었다고 가정
        String job = "학생";
        LocalDate birthday = LocalDate.of(2000, 12, 12);
        List<MarketingAgreementDto> marketingCodeList = List.of(new MarketingAgreementDto(MarketingCode.SEND_INFORMATION_TO_THIRD_PARTY, true));
        List<MarketingAgreementRequest> marketingCodeRequsetList = List.of(new MarketingAgreementRequest(MarketingCode.SEND_INFORMATION_TO_THIRD_PARTY, true));
        RegisterRequest request = new RegisterRequest(account, password, nickname, phoneNumber, gender, job, birthday, marketingCodeRequsetList);
        RegisterCommand command = new RegisterCommand(account, password, nickname, phoneNumber, gender, job, birthday, marketingCodeList);

        given(manageUserService.register(command)).willThrow(new BadRequestException(CommonErrorCode.BAD_REQUEST));

        // when & then
        // RestControllerAdvice 같은 설정이 따로 없어서 커스텀된 에러는 ServletException를 반환
        assertThrows(ServletException.class, () -> mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))));
    }

    @Test
    @WithMockUser
    @DisplayName("이미 회원가입한 유저라 회원가입 실패")
    void register_Throws_ResourceNotFoundException(){
        // given
        String account = "testAccount";
        String password = "testPW@111";
        String nickname = "닉네임";
        String phoneNumber = "010-1234-1234";
        Gender gender = Gender.MAN; // Gender enum이 M,F 등으로 정의되었다고 가정
        String job = "학생";
        LocalDate birthday = LocalDate.of(2000, 12, 12);
        List<MarketingAgreementDto> marketingCodeList = List.of(new MarketingAgreementDto(MarketingCode.SEND_INFORMATION_TO_THIRD_PARTY, true));
        List<MarketingAgreementRequest> marketingCodeRequsetList = List.of(new MarketingAgreementRequest(MarketingCode.SEND_INFORMATION_TO_THIRD_PARTY, true));
        RegisterRequest request = new RegisterRequest(account, password, nickname, phoneNumber, gender, job, birthday, marketingCodeRequsetList);
        RegisterCommand command = new RegisterCommand(account, password, nickname, phoneNumber, gender, job, birthday, marketingCodeList);

        given(manageUserService.register(command)).willThrow(new ResourceNotFoundException(UserErrorCode.ALREADY_EXIST_USER));

        // when & then
        // RestControllerAdvice 같은 설정이 따로 없어서 커스텀된 에러는 ServletException를 반환
        assertThrows(ServletException.class, () -> mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))));
    }

    @Test
    @WithMockUser
    @DisplayName("로그아웃 성공")
    void signOut_ShouldReturnOk() throws Exception {
        // given
        Long userId = 1L;
        CustomUserDetails customUserDetails = new CustomUserDetails(userId);

        // when & then
        mockMvc.perform(patch("/users/sign-out").with(user(customUserDetails)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("계정 삭제 성공")
    void deleteAccount_ShouldReturnOk() throws Exception {
        // given
        Long userId = 1L;
        CustomUserDetails customUserDetails = new CustomUserDetails(userId);

        // when & then
        mockMvc.perform(delete("/users/account").with(user(customUserDetails)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("토큰 새로고침 성공")
    void refreshToken_ShouldReturnJwtDto() throws Exception {
        // given
        TokenRefreshRequest request = new TokenRefreshRequest("refreshToken");
        TokenRefreshCommand command = new TokenRefreshCommand("refreshToken");
        JwtDto jwtDto = new JwtDto("newToken", "newRefreshToken");
        JwtResponse jwtResponse = new JwtResponse(jwtDto);
        Long userId = 1L;
        CustomUserDetails customUserDetails = new CustomUserDetails(userId);
        given(manageUserService.refreshToken(userId, command)).willReturn(jwtDto);

        // when & then
        mockMvc.perform(patch("/users/refresh")
                        .with(user(customUserDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(jwtResponse)));
    }

    @Test
    @DisplayName("계정 중복 확인 성공")
    void validateDuplicateAccount_ShouldReturnTrue() throws Exception {
        // given
        String account = "testAccount";

        given(validateUserService.validateAccount(account)).willReturn(true);

        // when & then
        mockMvc.perform(post("/users/validate/duplicateAccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(account)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("계정 중복 확인 실패")
    void validateDuplicateAccount_Failed_ShouldReturnFalse() throws Exception {
        // given
        String account = "testAccount";

        given(validateUserService.validateAccount(account)).willReturn(false);

        // when & then
        mockMvc.perform(post("/users/validate/duplicateAccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(account)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    @DisplayName("정규식에 맞지 않은 계정 데이터로 중복 확인시 HandlerMethodValidationException")
    void validateDuplicateAccount_ThrowsHandlerMethodValidationException_WhenInvalidData() throws Exception {
        // given
        String account = "testAccount!@#@!#";

        given(validateUserService.validateAccount(account)).willThrow(new BadRequestException(CommonErrorCode.BAD_REQUEST));

        // when & then
        mockMvc.perform(post("/users/validate/duplicateAccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(account)
                )
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof HandlerMethodValidationException));
    }

    @Test
    @WithMockUser
    @DisplayName("닉네임 중복 확인 성공")
    void validateDuplicateNickname_ShouldReturnTrue() throws Exception {
        // given
        String nickname = "testNick";

        given(validateUserService.validateNickname(nickname)).willReturn(true);

        // when & then
        mockMvc.perform(post("/users/validate/duplicateNickname")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(nickname)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @WithMockUser
    @DisplayName("닉네임 중복 확인 실패")
    void validateDuplicateNickname_Failed_ShouldReturnFalse() throws Exception {
        // given
        String nickname = "testNick";

        given(validateUserService.validateNickname(nickname)).willReturn(false);

        // when & then
        mockMvc.perform(post("/users/validate/duplicateNickname")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(nickname)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    @DisplayName("정규식에 맞지 않은 닉네임 데이터로 중복 확인시 HandlerMethodValidationException")
    void validateDuplicateNickname_ThrowsHandlerMethodValidationException_WhenInvalidData() throws Exception {
        // given
        String nickname = "testNickname!@#@!#";

        given(validateUserService.validateNickname(nickname)).willThrow(new BadRequestException(CommonErrorCode.BAD_REQUEST));

        // when & then
        mockMvc.perform(post("/users/validate/duplicateNickname")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(nickname)
                )
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof HandlerMethodValidationException));
    }

    @Test
    @DisplayName("인증 코드 확인 성공")
    void validateSmsCode_ShouldReturnTrue() throws Exception {
        // given
        String phoneNumber = "010-0000-0000";
        String code = "123123";
        ValidateSmsCodeRequest request = new ValidateSmsCodeRequest(phoneNumber, code);

        given(validateUserService.validateSmsCode(request.toCommand())).willReturn(true);

        // when & then
        mockMvc.perform(patch("/users/validate/smsCode")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("인증 코드 확인 실패")
    void validateSmsCode_Failed_ShouldReturnFalse() throws Exception {
        // given
        String phoneNumber = "010-0000-0000";
        String code = "123123";
        ValidateSmsCodeRequest request = new ValidateSmsCodeRequest(phoneNumber, code);

        given(validateUserService.validateSmsCode(request.toCommand())).willReturn(false);

        // when & then
        mockMvc.perform(patch("/users/validate/smsCode")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    @DisplayName("정규식에 맞지 않는 데이터로 인증 코드 확인시 HandlerMethodValidationException 발생")
    void validateSmsCode_ThrowsHandlerMethodValidationException_WhenInvalidData() throws Exception {
        // given
        String phoneNumber = "9999-0000-0000";
        String code = "123123";
        ValidateSmsCodeRequest request = new ValidateSmsCodeRequest(phoneNumber, code);

        given(validateUserService.validateSmsCode(request.toCommand())).willReturn(false);

        // when & then
        mockMvc.perform(patch("/users/validate/smsCode")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    @DisplayName("인증 코드 요청 성공")
    void requestSmsCode_ShouldReturnTrue() throws Exception {
        // given
        String phoneNumber = "010-0000-0000";

        given(validateUserService.requestSmsCode(phoneNumber)).willReturn(true);

        // when & then
        mockMvc.perform(post("/users/validate/smsCode")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(phoneNumber)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("인증 코드 요청 실패")
    void requestSmsCode_Failed_ShouldReturnFalse() throws Exception {
        // given
        String phoneNumber = "010-0000-0000";

        given(validateUserService.requestSmsCode(phoneNumber)).willReturn(false);

        // when & then
        mockMvc.perform(post("/users/validate/smsCode")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(phoneNumber)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    @DisplayName("정규식에 맞지 않는 전화번호로 인증 번호 요청시 Throws")
    void requestSmsCode_Throws_WhenInvalidData() throws Exception {
        // given
        String phoneNumber = "9910-0000-0000";

        given(validateUserService.requestSmsCode(phoneNumber)).willThrow(new BadRequestException(CommonErrorCode.BAD_REQUEST));

        // when & then
        mockMvc.perform(post("/users/validate/smsCode")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(phoneNumber)
                )
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof HandlerMethodValidationException));
    }
}