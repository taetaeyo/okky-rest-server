package com.okky.restserver.dto;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {
	
	@Schema(name = "UUID4")
	private UUID uuid;
	
	@Schema(name = "사용자 ID")
	private String id;
	
	@Schema(name = "사용자 비밀번호")
	private String password;
	
	@Schema(name = "이메일")
	private String email;
	
	@Schema(name = "실명")
	private String userName;
	
	@Schema(name = "닉네임")
	private String nickName;
	
}