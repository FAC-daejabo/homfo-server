package com.homfo.user.usecase;

/**
 * 직원이 로그아웃 합니다.
 * */
public interface SignOutUsecase {
    /**
     * 특정 직원이 로그아웃 합니다.
     * */
    void signOut(long employeeId);
}
