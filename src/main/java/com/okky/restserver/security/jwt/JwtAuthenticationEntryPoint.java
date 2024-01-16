package com.okky.restserver.security.jwt;

import java.io.IOException;

import org.json.simple.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.okky.restserver.aop.AuthenticationErrorCode;

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
        if(authorization == null) {
        	log.error("AcessToken Error : FAIL_AUTHENTICATION");
            setJsonResponse(response, AuthenticationErrorCode.FAIL_AUTHENTICATION);
        }
        //토큰 만료된 경우
        else if(authorization.equals(AuthenticationErrorCode.TOKEN_EXPIRED)) {
        	log.error("AcessToken Error : TOKEN_EXPIRED");
            setJsonResponse(response, AuthenticationErrorCode.TOKEN_EXPIRED);
        }
        // 미권한 토큰인 경우
        else if(authorization.equals(AuthenticationErrorCode.FAIL_AUTHORIZATION)) {
        	log.error("AcessToken Error : FAIL_AUTHORIZATION");
            setJsonResponse(response, AuthenticationErrorCode.FAIL_AUTHORIZATION);
        }
        else {
        	log.error("AcessToken Error : INTERNAL_SERVER_ERROR");
            setJsonResponse(response, AuthenticationErrorCode.INTERNAL_SERVER_ERROR);
        }
	}
	
	//한글 출력을 위해 getWriter() 사용
    private void setJsonResponse(HttpServletResponse response, AuthenticationErrorCode code) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        JSONObject responsObject = new JSONObject();
        responsObject.put("message", code.getMessage());
        responsObject.put("code", code.getHttpStatus().value());

        response.getWriter().print(responsObject);
    }

}
