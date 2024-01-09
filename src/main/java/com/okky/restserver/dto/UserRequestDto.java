package com.okky.restserver.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserRequestDto {
	private String userName;
	private String email;
	private String password;
	private String nickname;
}
