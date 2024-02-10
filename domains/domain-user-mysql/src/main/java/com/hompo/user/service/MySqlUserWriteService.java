package com.hompo.user.service;

import com.hompo.auth.dto.JwtDto;
import com.hompo.user.dto.UserDto;
import com.hompo.user.entity.MySqlUser;
import com.hompo.user.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hompo.user.command.SignInCommand;
import com.hompo.user.command.RegisterCommand;

import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class MySqlUserWriteService implements UserWriteService {
    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    @Transactional
    @Override
    public UserDto register(@NonNull RegisterCommand command) {
        MySqlUser user;
        Optional<MySqlUser> optionalUser = userRepository.findByAccountOrNicknameOrPhoneNumber(command.account(), command.nickname(), command.phoneNumber());

        if (optionalUser.isPresent()) {
            // TODO: 이미 존재하는 계정, 닉네임, 전화번호 임을 알려주기
            throw new RuntimeException();
        }

        user = MySqlUser.builder()
                .account(command.account())
                .password(encoder.encode(command.password()))
                .phoneNumber(command.phoneNumber())
                .nickname(command.nickname())
                .gender(command.gender())
                .birthday(command.birthday())
                .job(command.job())
                .build();

        userRepository.save(user);

        return new UserDto(user.getId(), user.getAccount(), user.getNickname(), user.getPhoneNumber(), user.getGender(), user.getJob(), user.getBirthday());
    }

    @Override
    public UserDto signIn(@NonNull SignInCommand command) {
        // TODO: 존재하지 않는 유저 알려주기
        MySqlUser user = userRepository.findByAccount(command.account()).orElseThrow(RuntimeException::new);

        user.signIn(encoder, command.password());

        userRepository.save(user);

        return null;
    }


    @Override
    public void deleteAccount(long userId) {
        // TODO: 존재하지 않는 유저 알려주기
        MySqlUser user = userRepository.findById(userId).orElseThrow(RuntimeException::new);

        user.deleteAccount();
        userRepository.save(user);
    }
}
