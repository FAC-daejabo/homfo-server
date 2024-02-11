package com.hompo.controller;

import com.hompo.auth.command.TokenRefreshCommand;
import com.hompo.auth.dto.JwtDto;
import com.hompo.auth.entity.CustomUserDetails;
import com.hompo.user.command.SignInCommand;
import com.hompo.user.dto.UserMarketingAgreementDto;
import com.hompo.user.service.UserRefreshTokenWriteService;
import com.hompo.user.service.UserWriteService;
import com.hompo.user.usecase.GetUserInfoUsecase;
import com.hompo.user.usecase.RegisterUsecase;
import com.hompo.user.usecase.SignInUsecase;
import com.hompo.user.usecase.TokenRefreshUsecase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.hompo.user.command.RegisterCommand;

@RequiredArgsConstructor
@Tag(name = "user-controller", description = "사용자 컨트롤러")
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserWriteService userWriteService;

    private final UserRefreshTokenWriteService userRefreshTokenWriteService;

    private final GetUserInfoUsecase getUserInfoUsecase;

    private final RegisterUsecase registerUsecase;

    private final SignInUsecase signInUsecase;

    private final TokenRefreshUsecase tokenRefreshUsecase;

    @Operation(summary = "사용자 정보 확인")
    @GetMapping("/info")
    public ResponseEntity<UserMarketingAgreementDto> info(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long userId = customUserDetails.userId();
        return ResponseEntity.status(HttpStatus.OK).body(getUserInfoUsecase.execute(userId));
    }

    @Operation(summary = "사용자 회원가입", description = "마케팅 동의 여부도 보내주셔야 합니다. 미동의더라도 false로 보내주세요.")
    @PostMapping("/register")
    public ResponseEntity<JwtDto> register(@RequestBody RegisterCommand command) {
        return ResponseEntity.status(HttpStatus.OK).body(registerUsecase.execute(command));
    }

    @Operation(summary = "사용자 로그인")
    @PostMapping("/sign-in")
    public ResponseEntity<JwtDto> signIn(@RequestBody SignInCommand command) {
        return ResponseEntity.status(HttpStatus.OK).body(signInUsecase.execute(command));
    }

    @Operation(summary = "사용자 로그아웃")
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/sign-out")
    public ResponseEntity<Void> signOut(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long userId = customUserDetails.userId();
        userRefreshTokenWriteService.deleteByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "사용자 계정 삭제")
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/account")
    public ResponseEntity<Void> deleteAccount(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long userId = customUserDetails.userId();
        userWriteService.deleteAccount(userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "사용자 액세스 토큰 새로고침", description = "헤더에 액세스 토큰 꼭 보내야합니다. 만료되더라도 보내주셔야 합니다.")
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/refresh")
    public ResponseEntity<JwtDto> refreshToken(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody TokenRefreshCommand command) {
        Long userId = customUserDetails.userId();
        return ResponseEntity.status(HttpStatus.OK).body(tokenRefreshUsecase.execute(userId, command));
    }
}
