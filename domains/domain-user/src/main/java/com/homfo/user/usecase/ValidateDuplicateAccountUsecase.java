package com.homfo.user.usecase;

/**
 * 사용자 계정이 중복되는지 확인합니다.
 * */
public interface ValidateDuplicateAccountUsecase {
    /**
     * 사용자 계정이 중복되는지 확인합니다.
     *
     * @throws com.homfo.error.ResourceAlreadyExistException 이미 존재하는 계정이라면
     * */
    void validate(String account);
}
