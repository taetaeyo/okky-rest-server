package com.okky.restserver.dto;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ResponseCode {
	
	S0000(HttpStatus.OK, "Success"),									// 200 : 성공
	E0000(HttpStatus.BAD_REQUEST, "The input value is invalied."),		// 400 : 입력 값 오류
	E0001(HttpStatus.UNAUTHORIZED, "Authentication failed."),			// 401 : 인증되지 않은 사용자
	E0002(HttpStatus.PAYMENT_REQUIRED, "Access token has expired."),	// 402 : 토근 만료
	E0003(HttpStatus.FORBIDDEN, "Authorization failed."),				// 403 : 권한이 없는 요청
	E0004(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error."),	// 500 : 서버 에러
	E0005(HttpStatus.BAD_REQUEST, "Check the x-api-key."),				// 400 : x-api-key 
	E0006(HttpStatus.CONFLICT, "You are already registered.");			// 409 : Resource의 현재 상태와 충돌. 보통 중복된 데이터 존재
	

	HttpStatus status;
	String message;
	
	ResponseCode(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}
	
}
