package com.okky.restserver.aop;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AuthenticationErrorCode {
	
	// 400 BAD_REQUEST : 잘못된 요청
	INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "INVALID INPUT VALUE"),
	X_API_KEY_ERROR(HttpStatus.BAD_REQUEST, "CHECK THE X-API-KEY"),
	
	// 401 UNAUTHORIZED : 인증되지 않은 사용자
    FAIL_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "FAIL AUTHENTICATION"),
    
    // 402 PAYMENT_REQUIRED : 토근 만료
    TOKEN_EXPIRED(HttpStatus.PAYMENT_REQUIRED, "TOKEN EXPIRED"),
    
    // 403 FORBIDDEN : 권한이 없는 요청
    FAIL_AUTHORIZATION(HttpStatus.FORBIDDEN, "FAIL AUTHORIZATION"),
    
    // 404 NOT_FOUND : Resource를 찾을 수 없음
 	NOT_FOUND(HttpStatus.NOT_FOUND, "THE REQUEST COULD NOT BE FOUND"),
    
    // 409 : CONFLICT : Resource의 현재 상태와 충돌. 보통 중복된 데이터 존재
 	DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "DATA ALREADY EXISTS"),
 	
    // 500 : INTERNAL SERVER ERROR
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL SERVER ERROR");

    private final HttpStatus httpStatus;
    private final String message;

}
