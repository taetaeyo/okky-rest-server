package com.okky.restserver.dto;

import com.okky.restserver.domain.User;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class UserResponseDto {
	
	@Schema(name = "UUID4")
	private String uuid;
	
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
	
    public static UserResponseDto from(User user) {
    	
    	if (user == null) return null;
    	
    	return UserResponseDto.builder()
    							.uuid(user.getUuid())
    							.id(user.getId())
    							.email(user.getEmail())
    							.userName(user.getUsername())
    							.nickName(user.getNickName())
    							.build();
    }
	
}
