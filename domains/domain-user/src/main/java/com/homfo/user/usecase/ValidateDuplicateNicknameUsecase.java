package com.homfo.user.usecase;

/**
 * 사용자 닉네임이 중복되는지 확인합니다.
 * */
public interface ValidateDuplicateNicknameUsecase {
    /**
     * 사용자 닉네임이 중복되는지 확인합니다.
     *
     * @throws com.homfo.error.ResourceAlreadyExistException 이미 존재하는 닉네임이라면
     * */
    boolean validateNickname(String nickname);
}
