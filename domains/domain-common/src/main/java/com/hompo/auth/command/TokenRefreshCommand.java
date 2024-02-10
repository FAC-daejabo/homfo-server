package com.hompo.auth.command;

public record TokenRefreshCommand(
        String token
) {
}
