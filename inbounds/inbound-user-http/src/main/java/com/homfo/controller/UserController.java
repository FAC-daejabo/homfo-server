package com.homfo.controller;

import com.homfo.auth.command.TokenRefreshCommand;
import com.homfo.auth.dto.JwtDto;
import com.homfo.auth.entity.CustomUserDetails;
import com.homfo.user.command.RegisterCommand;
import com.homfo.user.command.SignInCommand;
import com.homfo.user.dto.UserMarketingAgreementDto;
import com.homfo.user.usecase.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
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

    @Operation(summary = "사용자 정보 확인")
    @GetMapping("/info")
    public ResponseEntity<UserMarketingAgreementDto> info(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long userId = customUserDetails.userId();
        return ResponseEntity.status(HttpStatus.OK).body(getUserInfoUsecase.getUserInfo(userId));
    }

    @Operation(summary = "사용자 회원가입", description = "마케팅 동의 여부도 보내주셔야 합니다. 미동의더라도 false로 보내주세요.")
    @PostMapping("/register")
    public ResponseEntity<JwtDto> register(@RequestBody RegisterCommand command) {

        return ResponseEntity.status(HttpStatus.OK).body(registerUsecase.register(command));
    }

    @Operation(summary = "사용자 로그인")
    @PostMapping("/sign-in")
    public ResponseEntity<JwtDto> signIn(@RequestBody SignInCommand command) {
        return ResponseEntity.status(HttpStatus.OK).body(signInUsecase.signIn(command));
    }

    @Operation(summary = "사용자 로그아웃")
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/sign-out")
    public ResponseEntity<Void> signOut(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long userId = customUserDetails.userId();
        signOutUsecase.signOut(userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "사용자 계정 삭제")
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/account")
    public ResponseEntity<Void> deleteAccount(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long userId = customUserDetails.userId();
        deleteAccountUsecase.deleteAccount(userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "사용자 액세스 토큰 새로고침", description = "헤더에 액세스 토큰 꼭 보내야합니다. 만료되더라도 보내주셔야 합니다.")
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/refresh")
    public ResponseEntity<JwtDto> refreshToken(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody TokenRefreshCommand command) {
        Long userId = customUserDetails.userId();
        return ResponseEntity.status(HttpStatus.OK).body(tokenRefreshUsecase.refreshToken(userId, command));
    }
}
