package com.okky.restserver.aop;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.okky.restserver.dto.ErrorDto;
import com.okky.restserver.dto.ResponseCode;
import com.okky.restserver.dto.ResponseDto;

@SuppressWarnings("rawtypes")
@RestControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice{

	@Override
	public boolean supports(MethodParameter returnType, Class converterType) {
		return true;
	}
	
	@Override
	public Object beforeBodyWrite(
			Object body, MethodParameter returnType, 
			MediaType selectedContentType,
			Class selectedConverterType,
			ServerHttpRequest request, ServerHttpResponse response) {
		
		if(body instanceof ErrorDto || body instanceof ResponseDto) {
			return body;
		} else {
			return ResponseDto.builder()
					.status(ResponseCode.S0000.getStatus().value())
					.code(ResponseCode.S0000.name())
					.message(ResponseCode.S0000.getMessage())
					.result(body).build();
		}
		
	}
}
