package com.okky.restserver.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApiResponse<T> {
	
	private int statusCode;	// statusCode
	private String code;		// 성공, 실패 코드
    private String message;		// 성공, 실패 코드에 따른 message
    private Result<T> result;

    @Setter
    @Getter
    // result 필드를 나타내는 내부 클래스
    public static class Result<T> {
        private String type;
        private T data;
    }
}
