package user.infra.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum UserStatus {
    PENDING("P"),

    USE("N"),
    DELETED("Y"),
    STOPPED("F");

    private final String code;

    UserStatus(String code) {
        this.code = code;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    @JsonCreator
    public static UserStatus fromCode(String code) {
        return Arrays.stream(UserStatus.values())
                .filter(v -> v.getCode().equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(String.format("유저 상태에 %s가 존재하지 않습니다.", code)));
    }
}