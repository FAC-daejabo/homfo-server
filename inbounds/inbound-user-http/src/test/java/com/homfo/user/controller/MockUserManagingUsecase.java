package com.homfo.user.controller;

import com.homfo.auth.command.TokenRefreshCommand;
import com.homfo.auth.dto.JwtDto;
import com.homfo.user.command.RegisterCommand;
import com.homfo.user.command.SignInCommand;
import com.homfo.user.usecase.*;

class MockUserManagingUsecase implements DeleteAccountUsecase, RegisterUsecase, SignInUsecase, SignOutUsecase, TokenRefreshUsecase {

    @Override
    public void deleteAccount(long employeeId) {

    }

    @Override
    public JwtDto register(RegisterCommand command) {
        return null;
    }

    @Override
    public JwtDto signIn(SignInCommand command) {
        return null;
    }

    @Override
    public void signOut(long employeeId) {

    }

    @Override
    public JwtDto refreshToken(long employeeId, TokenRefreshCommand command) {
        return null;
    }
}