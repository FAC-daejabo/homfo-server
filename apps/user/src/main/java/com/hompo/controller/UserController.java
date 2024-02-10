package com.hompo.controller;

import com.hompo.auth.dto.JwtDto;
import com.hompo.usecase.RegisterUsecase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hompo.user.dto.RegisterCommand;

@RequiredArgsConstructor
@Tag(name = "user-controller", description = "사용자 컨트롤러")
@RestController
@RequestMapping("/users")
public class UserController {
    private final RegisterUsecase registerUsecase;

    @Operation(summary = "사용자 회원가입")
    @PostMapping("/register")
    public ResponseEntity<JwtDto> register(@RequestBody RegisterCommand command) {
        return ResponseEntity.status(HttpStatus.OK).body(registerUsecase.execute(command));
    }
}
