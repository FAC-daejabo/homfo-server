package com.homfo.employee.request;

import com.homfo.auth.command.TokenRefreshCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record TokenRefreshRequest(
        @Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c", description = "리프레쉬 토큰")
        @NotNull
        String token
) {
    public TokenRefreshCommand toCommand() {
        return new TokenRefreshCommand(token);
    }
}
