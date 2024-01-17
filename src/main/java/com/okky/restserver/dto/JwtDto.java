package com.okky.restserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class JwtDto {
	private String code;		// status 코드
	private String authType;	// Bearer
	private String jwt;
}
