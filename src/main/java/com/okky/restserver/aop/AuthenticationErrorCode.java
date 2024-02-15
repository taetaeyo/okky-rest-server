package com.okky.restserver.aop;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AuthenticationErrorCode {
	
	INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "INVALID INPUT VALUE"),
    FAIL_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "FAIL AUTHENTICATION"), 				// 토큰 불일치
    TOKEN_EXPIRED(HttpStatus.PAYMENT_REQUIRED, "TOKEN EXPIRED"), 						// 토큰 만료
    FAIL_AUTHORIZATION(HttpStatus.FORBIDDEN, "FAIL AUTHORIZATION"), 					// 권한 없는 요청
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL SERVER ERROR"), 	// 서버 에러
	
	X_API_KEY_ERROR(HttpStatus.BAD_REQUEST, "x-api-key를 확인하세요."); 						// x-api-key 에러

    private final HttpStatus httpStatus;
    private final String message;

}
