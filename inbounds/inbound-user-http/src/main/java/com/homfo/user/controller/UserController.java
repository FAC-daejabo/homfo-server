package com.homfo.user.controller;

import com.homfo.auth.dto.JwtDto;
import com.homfo.auth.entity.CustomUserDetails;
import com.homfo.user.dto.UserMarketingAgreementDto;
import com.homfo.user.request.RegisterRequest;
import com.homfo.user.request.SignInRequest;
import com.homfo.user.request.TokenRefreshRequest;
import com.homfo.user.response.JwtResponse;
import com.homfo.user.response.UserMarketingAgreementResponse;
import com.homfo.user.usecase.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @Autowired
    public UserController(
            @Qualifier("manageUserService") DeleteAccountUsecase deleteAccountUsecase,
            GetUserInfoUsecase getUserInfoUsecase,
            @Qualifier("manageUserService") RegisterUsecase registerUsecase,
            @Qualifier("manageUserService") SignInUsecase signInUsecase,
            @Qualifier("manageUserService") SignOutUsecase signOutUsecase,
            @Qualifier("manageUserService") TokenRefreshUsecase tokenRefreshUsecase
    ) {
        this.deleteAccountUsecase = deleteAccountUsecase;
        this.getUserInfoUsecase = getUserInfoUsecase;
        this.registerUsecase = registerUsecase;
        this.signInUsecase = signInUsecase;
        this.signOutUsecase = signOutUsecase;
        this.tokenRefreshUsecase = tokenRefreshUsecase;
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
    public ResponseEntity<JwtResponse> register(@RequestBody RegisterRequest request) {
        JwtDto jwtDto = registerUsecase.register(request.toCommand());
        return ResponseEntity.status(HttpStatus.OK).body(new JwtResponse(jwtDto));
    }

    @Operation(summary = "사용자 로그인")
    @PostMapping("/sign-in")
    public ResponseEntity<JwtResponse> signIn(@RequestBody SignInRequest request) {
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
}
