package com.okky.restserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class JwtDto {
	private String grantType;	// Bearer
	private String jwt;
	private String refreshToken;
}
