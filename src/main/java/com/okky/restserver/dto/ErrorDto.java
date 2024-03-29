package com.okky.restserver.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ErrorDto {
	private final int status;
	private final String code;
	private final String message;
}