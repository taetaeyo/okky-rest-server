package com.okky.restserver.dto;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ResponseCode {
	
	S0000(HttpStatus.OK, "Success"),
	E0000(HttpStatus.BAD_REQUEST, "The input value is invalied."),
	E0001(HttpStatus.UNAUTHORIZED, "Authentication failed."),
	E0002(HttpStatus.PAYMENT_REQUIRED, "Token has expired."),
	E0003(HttpStatus.FORBIDDEN, "Authorization failed."),
	E0004(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error."),
	E0005(HttpStatus.BAD_REQUEST, "Check X-API-KEY."),
	E0006(HttpStatus.CONFLICT, "This user is already registered.");
	

	HttpStatus status;
	String message;
	
	ResponseCode(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}
	
}
