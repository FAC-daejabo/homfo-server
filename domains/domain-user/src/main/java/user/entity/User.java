package user.entity;

import user.infra.enums.UserStatus;

import java.time.LocalDateTime;

public interface User {
    Long getId();

    String getAccount();

    String getPassword();

    String getPhoneNumber();

    String getNickname();

    String getGender();


    String getBirthday();

    String getJob();

    UserStatus getStatus();

    String getRefreshToken();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();
}
