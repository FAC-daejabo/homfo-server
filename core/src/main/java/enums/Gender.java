package enums;

import java.util.Arrays;

public enum Gender {
    MAN("M"),

    WOMAN("N");
    private final String code;

    Gender(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static Gender fromCode(String code) {
        return Arrays.stream(values())
                .filter(v -> v.getCode().equals(code))
                .findAny()
                .orElse(null);
    }
}