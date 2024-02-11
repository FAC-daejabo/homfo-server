package com.hompo.user.service;

import com.hompo.user.dto.UserDto;

public interface UserReadService {
    public UserDto findById(long userId);
}
