package com.okky.restserver.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserResponseDto {
	private String id;
	private String jwt;
}
