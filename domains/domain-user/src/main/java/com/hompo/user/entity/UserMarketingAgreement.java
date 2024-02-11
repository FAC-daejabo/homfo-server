package com.hompo.user.entity;

import com.hompo.enums.MarketingCode;

import java.time.LocalDateTime;

public abstract class UserMarketingAgreement {
    public abstract Long getId();

    public abstract Long getUserId();

    public abstract MarketingCode getCode();

    public abstract boolean getAgreement();

    public abstract LocalDateTime getCreatedAt();

    public abstract LocalDateTime getUpdatedAt();

    public abstract boolean isAgree();
}
