package user.infra.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import user.infra.enums.UserStatus;

@Component
public class StringToUserStatusConverter implements Converter<String, UserStatus> {
    @Override
    public UserStatus convert(@NonNull String source) {
        return UserStatus.fromCode(source);
    }
}