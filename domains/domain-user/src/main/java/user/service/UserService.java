package user.service;

import user.dto.SignInCommand;
import user.dto.SignInDto;
import user.dto.SignUpCommand;

public interface UserService {
    SignInDto signUp(SignUpCommand command);

    SignInDto signIn(SignInCommand command);

    void signOut(Long userId);
}
