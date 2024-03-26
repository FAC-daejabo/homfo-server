package com.homfo.user.controller;

import com.homfo.auth.dto.JwtDto;
import com.homfo.auth.entity.CustomUserDetails;
import com.homfo.sms.usecase.RequestSmsCodeUsecase;
import com.homfo.sms.usecase.ValidateSmsCodeUsecase;
import com.homfo.user.dto.UserMarketingAgreementDto;
import com.homfo.user.request.RegisterRequest;
import com.homfo.user.request.SignInRequest;
import com.homfo.user.request.TokenRefreshRequest;
import com.homfo.user.request.ValidateSmsCodeRequest;
import com.homfo.user.response.JwtResponse;
import com.homfo.user.response.UserMarketingAgreementResponse;
import com.homfo.user.usecase.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@Tag(name = "user-controller", description = "사용자 컨트롤러")
@RestController
@RequestMapping("/users")
public class UserController {
    private final DeleteAccountUsecase deleteAccountUsecase;

    private final GetUserInfoUsecase getUserInfoUsecase;

    private final RegisterUsecase registerUsecase;

    private final SignInUsecase signInUsecase;

    private final SignOutUsecase signOutUsecase;

    private final TokenRefreshUsecase tokenRefreshUsecase;

    private final ValidateDuplicateAccountUsecase validateDuplicateAccountUsecase;

    private final ValidateDuplicateNicknameUsecase validateDuplicateNicknameUsecase;

    private final ValidateSmsCodeUsecase validateSmsCodeUsecase;

    private final RequestSmsCodeUsecase requestSmsCodeUsecase;

    @Autowired
    public UserController(
            @Qualifier("manageUserService") DeleteAccountUsecase deleteAccountUsecase,
            GetUserInfoUsecase getUserInfoUsecase,
            @Qualifier("manageUserService") RegisterUsecase registerUsecase,
            @Qualifier("manageUserService") SignInUsecase signInUsecase,
            @Qualifier("manageUserService") SignOutUsecase signOutUsecase,
            @Qualifier("manageUserService") TokenRefreshUsecase tokenRefreshUsecase,
            @Qualifier("validateUserService") ValidateDuplicateAccountUsecase validateDuplicateAccountUsecase,
            @Qualifier("validateUserService") ValidateDuplicateNicknameUsecase validateDuplicateNicknameUsecase,
            @Qualifier("validateUserService") ValidateSmsCodeUsecase validateSmsCodeUsecase,
            @Qualifier("validateUserService") RequestSmsCodeUsecase requestSmsCodeUsecase
    ) {
        this.deleteAccountUsecase = deleteAccountUsecase;
        this.getUserInfoUsecase = getUserInfoUsecase;
        this.registerUsecase = registerUsecase;
        this.signInUsecase = signInUsecase;
        this.signOutUsecase = signOutUsecase;
        this.tokenRefreshUsecase = tokenRefreshUsecase;
        this.validateDuplicateAccountUsecase = validateDuplicateAccountUsecase;
        this.validateDuplicateNicknameUsecase = validateDuplicateNicknameUsecase;
        this.validateSmsCodeUsecase = validateSmsCodeUsecase;
        this.requestSmsCodeUsecase = requestSmsCodeUsecase;
    }

    @Operation(summary = "사용자 정보 확인")
    @GetMapping("/info")
    public ResponseEntity<UserMarketingAgreementResponse> info(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long userId = customUserDetails.id();
        UserMarketingAgreementDto dto = getUserInfoUsecase.getUserInfo(userId);
        return ResponseEntity.status(HttpStatus.OK).body(new UserMarketingAgreementResponse(dto));
    }

    @Operation(summary = "사용자 회원가입", description = "마케팅 동의 여부도 보내주셔야 합니다. 미동의더라도 false로 보내주세요.")
    @PostMapping("/register")
    public ResponseEntity<JwtResponse> register(
            @Valid
            @RequestBody
            RegisterRequest request
    ) {
        JwtDto jwtDto = registerUsecase.register(request.toCommand());
        return ResponseEntity.status(HttpStatus.OK).body(new JwtResponse(jwtDto));
    }

    @Operation(summary = "사용자 로그인")
    @PostMapping("/sign-in")
    public ResponseEntity<JwtResponse> signIn(
            @Valid
            @RequestBody
            SignInRequest request
    ) {
        JwtDto jwtDto = signInUsecase.signIn(request.toCommand());
        return ResponseEntity.status(HttpStatus.OK).body(new JwtResponse(jwtDto));
    }

    @Operation(summary = "사용자 로그아웃")
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/sign-out")
    public ResponseEntity<Void> signOut(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long userId = customUserDetails.id();
        signOutUsecase.signOut(userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "사용자 계정 삭제")
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/account")
    public ResponseEntity<Void> deleteAccount(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long userId = customUserDetails.id();
        deleteAccountUsecase.deleteAccount(userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "사용자 액세스 토큰 새로고침", description = "헤더에 액세스 토큰 꼭 보내야합니다. 만료되더라도 보내주셔야 합니다.")
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/refresh")
    public ResponseEntity<JwtResponse> refreshToken(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody TokenRefreshRequest request) {
        Long userId = customUserDetails.id();
        JwtDto dto = tokenRefreshUsecase.refreshToken(userId, request.toCommand());
        return ResponseEntity.status(HttpStatus.OK).body(new JwtResponse(dto));
    }

    @Operation(summary = "사용자 계정 중복 확인")
    @PostMapping("/validate/duplicateAccount")
    public ResponseEntity<Boolean> validateDuplicateAccount(
            @Valid
            @RequestBody
            @Schema(example = "testAccount", description = "계정")
            @Pattern(regexp = "^[a-zA-Z\\d]{8,15}$", message = "올바르지 않은 계정 또는 비밀번호입니다.")
            String account
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(validateDuplicateAccountUsecase.validateAccount(account));
    }

    @Operation(summary = "사용자 닉네임 중복 확인")
    @PostMapping("/validate/duplicateNickname")
    public ResponseEntity<Boolean> validateDuplicateNickname(
            @Valid
            @RequestBody
            @Schema(example = "닉네임", description = "닉네임")
            @Pattern(regexp = "^[a-zA-Z\\\\d가-힣]{1,15}$", message = "올바르지 않은 닉네임입니다.")
            String nickname
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(validateDuplicateNicknameUsecase.validateNickname(nickname));
    }

    @Operation(summary = "사용자 전화번호 인증 코드 확인")
    @PatchMapping("/validate/smsCode")
    public ResponseEntity<Boolean> validateSmsCode(
            @Valid
            @RequestBody
            ValidateSmsCodeRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(validateSmsCodeUsecase.validateSmsCode(request.toCommand()));
    }

    @Operation(summary = "사용자 닉네임 인증 코드 요청", description = "Body 보내실 때 전화번호에서 따옴표 제거하고 보내주세요.")
    @PostMapping("/validate/smsCode")
    public ResponseEntity<Boolean> requestSmsCode(
            @Valid
            @RequestBody
            @Schema(example = "010-0000-0000", description = "전화번호")
            @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "-가 포함된 모바일 전화번호 11자리여야 합니다.")
            String phoneNumber
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(requestSmsCodeUsecase.requestSmsCode(phoneNumber));
    }
}
