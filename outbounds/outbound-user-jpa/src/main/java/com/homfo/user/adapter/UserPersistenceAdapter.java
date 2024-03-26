package com.homfo.user.adapter;

import com.homfo.error.ResourceAlreadyExistException;
import com.homfo.error.ResourceNotFoundException;
import com.homfo.user.command.RegisterCommand;
import com.homfo.user.command.SignInCommand;
import com.homfo.user.dto.UserDto;
import com.homfo.user.entity.JpaUser;
import com.homfo.user.infra.enums.UserErrorCode;
import com.homfo.user.infra.enums.UserStatus;
import com.homfo.user.infra.util.ValidationUtil;
import com.homfo.user.port.LoadUserPort;
import com.homfo.user.port.ManageUserPort;
import com.homfo.user.repository.UserRepository;
import com.homfo.user.repository.UserSmsCodeRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserPersistenceAdapter implements LoadUserPort, ManageUserPort {
    private final UserRepository userRepository;

    private final UserSmsCodeRepository userSmsCodeRepository;

    private final PasswordEncoder encoder;

    @Override
    public UserDto loadUser(long userId) {
        JpaUser user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(UserErrorCode.NOT_EXIST_USER));
        return new UserDto(user.getId(), user.getAccount(), user.getNickname(), user.getPhoneNumber(), user.getGender(), user.getJob(), user.getBirthday(), user.getStatus());
    }

    @Override
    public UserDto signIn(@NonNull SignInCommand command) {
        JpaUser user;

        ValidationUtil.validateUserOriginPassword(command.password());

        user = userRepository.findByAccount(command.account()).orElseThrow(() -> new ResourceNotFoundException(UserErrorCode.NOT_EXIST_USER));

        user.signIn(encoder, command.password());

        return new UserDto(user.getId(), user.getAccount(), user.getNickname(), user.getPhoneNumber(), user.getGender(), user.getJob(), user.getBirthday(), user.getStatus());
    }

    @Override
    @Transactional
    public UserDto register(@NonNull RegisterCommand command) {
        JpaUser user;
        Optional<JpaUser> optionalUser;

        ValidationUtil.validateUserOriginPassword(command.password());

        optionalUser = userRepository.findByAccountOrNicknameOrPhoneNumber(command.account(), command.nickname(), command.phoneNumber());

        if (optionalUser.isPresent()) {
            throw new ResourceAlreadyExistException(UserErrorCode.ALREADY_EXIST_USER);
        }

        user = JpaUser.builder()
                .account(command.account())
                .password(encoder.encode(command.password()))
                .phoneNumber(command.phoneNumber())
                .nickname(command.nickname())
                .gender(command.gender())
                .birthday(command.birthday())
                .job(command.job())
                .build();

        userRepository.save(user);

        return new UserDto(user.getId(), user.getAccount(), user.getNickname(), user.getPhoneNumber(), user.getGender(), user.getJob(), user.getBirthday(), user.getStatus());
    }

    @Override
    public void deleteAccount(long userId) {
        JpaUser user = userRepository.findByIdAndStatusNot(userId, UserStatus.DELETED).orElseThrow(() -> new ResourceNotFoundException(UserErrorCode.NOT_EXIST_USER));

        user.deleteAccount();
        userRepository.save(user);
    }

    @Override
    public boolean existAccount(@NonNull String account) {
        return userRepository.existsByAccount(account);
    }

    @Override
    public boolean existNickname(@NonNull String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    @Override
    public boolean existPhoneNumber(@NonNull String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }
}
