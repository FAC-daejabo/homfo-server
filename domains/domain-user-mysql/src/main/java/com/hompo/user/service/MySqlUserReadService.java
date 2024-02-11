package com.hompo.user.service;

import com.hompo.user.command.SignInCommand;
import com.hompo.user.dto.UserDto;
import com.hompo.user.entity.MySqlUser;
import com.hompo.user.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MySqlUserReadService implements UserReadService {
    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    @Override
    public UserDto findById(long userId) {
        // TODO: 에러 정의
        MySqlUser user = userRepository.findById(userId).orElseThrow(RuntimeException::new);
        return new UserDto(user.getId(), user.getAccount(), user.getNickname(), user.getPhoneNumber(), user.getGender(), user.getJob(), user.getBirthday(), user.getStatus());
    }

    @Override
    public UserDto signIn(@NonNull SignInCommand command) {
        // TODO: 존재하지 않는 유저 알려주기
        MySqlUser user = userRepository.findByAccount(command.account()).orElseThrow(RuntimeException::new);

        user.signIn(encoder, command.password());

        return new UserDto(user.getId(), user.getAccount(), user.getNickname(), user.getPhoneNumber(), user.getGender(), user.getJob(), user.getBirthday(), user.getStatus());
    }
}
