package com.homfo.employee.controller;

import com.homfo.auth.dto.JwtDto;
import com.homfo.auth.entity.CustomUserDetails;
import com.homfo.employee.command.SignInCommand;
import com.homfo.employee.dto.EmployeeMarketingAgreementDto;
import com.homfo.employee.request.RegisterRequest;
import com.homfo.employee.request.SignInRequest;
import com.homfo.employee.request.TokenRefreshRequest;
import com.homfo.employee.response.EmloyeeMarketingAgreementResponse;
import com.homfo.employee.response.JwtResponse;
import com.homfo.employee.usecase.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "employee-controller", description = "직원 컨트롤러")
@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private final DeleteAccountUsecase deleteAccountUsecase;

    private final GetEmployeeInfoUsecase getEmployeeInfoUsecase;

    private final RegisterUsecase registerUsecase;

    private final SignInUsecase signInUsecase;

    private final SignOutUsecase signOutUsecase;

    private final TokenRefreshUsecase tokenRefreshUsecase;

    @Autowired
    public EmployeeController(
            @Qualifier("manageEmployeeService") DeleteAccountUsecase deleteAccountUsecase,
            GetEmployeeInfoUsecase getEmployeeInfoUsecase,
            @Qualifier("manageEmployeeService") RegisterUsecase registerUsecase,
            @Qualifier("manageEmployeeService") SignInUsecase signInUsecase,
            @Qualifier("manageEmployeeService") SignOutUsecase signOutUsecase,
            @Qualifier("manageEmployeeService") TokenRefreshUsecase tokenRefreshUsecase
    ) {
        this.deleteAccountUsecase = deleteAccountUsecase;
        this.getEmployeeInfoUsecase = getEmployeeInfoUsecase;
        this.registerUsecase = registerUsecase;
        this.signInUsecase = signInUsecase;
        this.signOutUsecase = signOutUsecase;
        this.tokenRefreshUsecase = tokenRefreshUsecase;
    }

    @Operation(summary = "사용자 정보 확인")
    @GetMapping("/info")
    public ResponseEntity<EmloyeeMarketingAgreementResponse> info(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long employeeId = customUserDetails.id();
        EmployeeMarketingAgreementDto dto = getEmployeeInfoUsecase.getEmployeeInfo(employeeId);
        return ResponseEntity.status(HttpStatus.OK).body(new EmloyeeMarketingAgreementResponse(dto));
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
        SignInCommand command = request.toCommand();
        JwtDto jwtDto = signInUsecase.signIn(command);
        return ResponseEntity.status(HttpStatus.OK).body(new JwtResponse(jwtDto));
    }

    @Operation(summary = "사용자 로그아웃")
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/sign-out")
    public ResponseEntity<Void> signOut(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long employeeId = customUserDetails.id();
        signOutUsecase.signOut(employeeId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "사용자 계정 삭제")
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/account")
    public ResponseEntity<Void> deleteAccount(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long employeeId = customUserDetails.id();
        deleteAccountUsecase.deleteAccount(employeeId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "사용자 액세스 토큰 새로고침", description = "헤더에 액세스 토큰 꼭 보내야합니다. 만료되더라도 보내주셔야 합니다.")
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/refresh")
    public ResponseEntity<JwtResponse> refreshToken(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody TokenRefreshRequest request) {
        Long employeeId = customUserDetails.id();
        JwtDto dto = tokenRefreshUsecase.refreshToken(employeeId, request.toCommand());
        return ResponseEntity.status(HttpStatus.OK).body(new JwtResponse(dto));
    }
}