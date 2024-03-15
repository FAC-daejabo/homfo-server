package com.homfo.user.usecase;

/**
 * 사용자가 로그아웃 합니다.
 * */
public interface SignOutUsecase {
    /**
     * 특정 사용자가 로그아웃 합니다.
     * */
    void signOut(long userId);
}
