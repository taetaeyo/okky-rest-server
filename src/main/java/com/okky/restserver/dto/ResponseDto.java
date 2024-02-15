package com.okky.restserver.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ResponseDto<T> {
	private final int status;
	private final String code;
	private final String message;
	private final T result;
}
