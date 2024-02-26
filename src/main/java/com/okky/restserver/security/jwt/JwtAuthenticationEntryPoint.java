package com.okky.restserver.security.jwt;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.okky.restserver.dto.ErrorDto;
import com.okky.restserver.dto.ResponseCode;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		
		
	    
		final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

		// 예외 유형에 따라 적절한 응답을 생성
		if(request.getAttribute("exception") != null && request.getAttribute("exception") == ResponseCode.E0005) {
        	log.error("AccessToken Error : x-api-key is null");
        	
        	//resolver.resolveException(request, response, null, (Exception) request.getAttribute("exception"));

        } else if(authorization == null) {
        	log.error("AccessToken Error : FAIL_AUTHENTICATION");
            setJsonResponse(response, ResponseCode.E0001);
        }
        // 토큰 만료된 경우
        else if(request.getAttribute("exception") != null && ResponseCode.E0002.name().equals(request.getAttribute("exception").toString())) {
        	log.error("AccessToken Error : TOKEN_EXPIRED");
            setJsonResponse(response, ResponseCode.E0002);
        }
        // 미권한 토큰인 경우 (현재 권한이 나뉘어져 있지 않음)
        else if(authorization.equals(ResponseCode.E0003.name())) {
        	log.error("AccessToken Error : FAIL_AUTHORIZATION");
            setJsonResponse(response, ResponseCode.E0003);
        }
        // 400 error
        else if(authorization.equals(ResponseCode.E0000.name())) {
        	log.error("AccessToken Error : INVALID_INPUT_VALUE");
            setJsonResponse(response, ResponseCode.E0000);
        } else {
        	log.error("AccessToken Error : INTERNAL_SERVER_ERROR");
            setJsonResponse(response, ResponseCode.E0004);
        }
	}
	
	//한글 출력을 위해 getWriter() 사용
    private void setJsonResponse(HttpServletResponse response, ResponseCode code) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        
        ObjectMapper mapper = new ObjectMapper();
        String jsonObj = mapper.writeValueAsString(ErrorDto.builder()
        		.status(code.getStatus().value())
        		.code(code.name())
        		.message(code.getMessage()).build());

        log.debug(jsonObj);

        response.getWriter().print(jsonObj);
    }

}
