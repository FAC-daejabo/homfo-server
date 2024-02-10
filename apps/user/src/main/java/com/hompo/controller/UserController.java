package com.hompo.controller;

import com.hompo.auth.dto.JwtDto;
import com.hompo.auth.entity.CustomUserDetails;
import com.hompo.usecase.RegisterUsecase;
import com.hompo.usecase.SignInUsecase;
import com.hompo.user.command.SignInCommand;
import com.hompo.user.service.UserRefreshTokenReadService;
import com.hompo.user.service.UserRefreshTokenWriteService;
import com.hompo.user.service.UserWriteService;
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

    private final RegisterUsecase registerUsecase;

    private final SignInUsecase signInUsecase;

    @Operation(summary = "사용자 회원가입")
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
    public ResponseEntity<JwtDto> signOut(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long userId = customUserDetails.getUserId();
        userRefreshTokenWriteService.deleteByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "사용자 계정 삭제")
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/account")
    public ResponseEntity<JwtDto> deleteAccount(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long userId = customUserDetails.getUserId();
        userWriteService.deleteAccount(userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
