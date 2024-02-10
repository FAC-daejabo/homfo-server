package com.hompo.user.service;

import com.hompo.auth.dto.JwtDto;
import com.hompo.user.entity.MySqlUser;
import com.hompo.user.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.hompo.user.dto.SignInCommand;
import com.hompo.user.dto.RegisterCommand;

import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class MySqlUserWriteService implements UserWriteService {
    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    @Transactional
    @Override
    public JwtDto register(@NonNull RegisterCommand command, @NonNull Function<Long, String> getAccessToken, @NonNull Function<Long, String> getRefreshToken) {
        MySqlUser user;
        JwtDto jwtDto;
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

        jwtDto = new JwtDto(getAccessToken.apply(user.getId()), getRefreshToken.apply(user.getId()));

        user.signUp(encoder, jwtDto);

        return jwtDto;
    }

    @Override
    public JwtDto signIn(@NonNull SignInCommand command, @NonNull JwtDto jwtDto) {
        // TODO: 존재하지 않는 유저 알려주기
        MySqlUser user = userRepository.findByAccount(command.account()).orElseThrow(RuntimeException::new);

        user.signIn(encoder, command.password(), jwtDto);

        userRepository.save(user);

        return null;
    }

    @Override
    public void signOut(long userId) {

    }

    @Override
    public void deleteAccount(long userId) {

    }
}
