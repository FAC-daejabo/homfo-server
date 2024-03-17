package com.homfo.employee.command;

/**
 * 로그인을 위한 Command 입니다.
 * */
public record SignInCommand(
        String account,

        String password
) {
}
