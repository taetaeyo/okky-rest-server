package com.okky.restserver.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "로그인 응답 DTO")
@Getter
public class SignInDto {
	
	@Schema(description = "사용자ID", nullable = false, example = "test01")
	private String id;
	
	@Schema(description = "비밀번호", nullable = false, example = "1234")
	private String password;
}
