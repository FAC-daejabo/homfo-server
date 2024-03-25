package com.homfo.user.request;

import com.homfo.sms.command.ValidateSmsCodeCommand;

public record ValidateSmsCodeRequest(
        String phoneNumber,

        String code
) {
    public ValidateSmsCodeCommand toCommand() {
        return new ValidateSmsCodeCommand(phoneNumber, code);
    }
}
