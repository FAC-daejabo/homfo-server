package com.homfo.sms.usecase;

/**
 * 사용자 전화번호가 중복되는지, 올바른 전화번호인지 확인합니다.
 * */
public interface RequestSmsCodeUsecase {
    /**
     * 사용자 전화번호가 중복되는지, 올바른 전화번호인지 확인합니다.
     *
     * @throws com.homfo.error.BadRequestException 올바르지 않는 전화번호 양식이라면
     * @throws com.homfo.error.ResourceAlreadyExistException 이미 존재하는 전화번호라면
     * @throws com.homfo.error.ResourceNotFoundException 존재하지 않는 전화번호라면
     * @throws com.homfo.error.ThirdPartyUnavailableException 외부 인증 서비스에서 장애가 발생한 경우
     * */
    boolean requestSmsCode(String phoneNumber);
}