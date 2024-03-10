package com.homfo.employee.usecase;

/**
 * 직원 계정 정보를 삭제합니다.
 * */
public interface DeleteAccountUsecase {
    /**
     * 직원 계정 삭제와 Refresh token 삭제를 한 트랜잭션으로 묶어야 합니다.
     *
     * @throws com.homfo.error.ResourceNotFoundException 존재하지 않는 직원이라면
     * */
    void deleteAccount(long employeeId);
}
