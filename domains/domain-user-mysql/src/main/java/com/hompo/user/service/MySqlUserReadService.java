package com.hompo.user.service;

import com.hompo.user.dto.UserDto;
import com.hompo.user.entity.MySqlUser;
import com.hompo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MySqlUserReadService implements UserReadService {
    private final UserRepository userRepository;

    public UserDto findById(long userId) {
        // TODO: 에러 정의
        MySqlUser user = userRepository.findById(userId).orElseThrow(RuntimeException::new);
        return new UserDto(user.getId(), user.getAccount(), user.getNickname(), user.getPhoneNumber(), user.getGender(), user.getJob(), user.getBirthday(), user.getStatus());
    }
}
