package com.okky.restserver.security.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.SignatureException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.okky.restserver.security.SecurityConstants;
import com.okky.restserver.security.jwt.JwtProvider;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Spring Security 환경설정을 구성하는 단계에서 Filter로 등록한 클래스
 * 지정한 URL 별 JWT 유효성 검증을 수행하며 직접적인 사용자 '인증'을 확인
 * 
 * @author taekwon
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter{
	
	private final JwtProvider jwtProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {
		// 토큰이 필요하지 않은 API URL에 대해서 배열로 구성
		List<String> list = Arrays.asList(
				"/test", 
//				"/test/user", 
				"/auth/sign-in"
		);

		// 토큰이 필요하지 않은 API URL의 경우 : 로직 처리 없이 다음 필터로 이동
		if (list.contains(request.getRequestURI())) {
			filterChain.doFilter(request, response);
			return;
		}

		// Header Check
		String authorizationHeader = request.getHeader(SecurityConstants.HEADER_AUTHORIZATION);
		log.info("AuthorizationHeader Check {}" + authorizationHeader);

		String token = getAccessToken(authorizationHeader);
		log.info("Access Token {}" + token);
		
		try {
			// 토큰 유효 check
			if (jwtProvider.validToken(token)) {
				// 인증 정보 설정
				Authentication authentication = jwtProvider.getAuthentication(token);
	            SecurityContextHolder.getContext().setAuthentication(authentication);
			} else {	// 토큰 유효하지 않음
				log.error("Token validation Fail");
			}
			
			filterChain.doFilter(request, response);
			
		} catch (Exception e) {
			// 토큰 Exception 발생 하였을 경우 : 클라이언트에 응답값을 반환하고 종료
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json");
			
			PrintWriter printWriter = response.getWriter();
			JSONObject jsonObject = jsonResponseWrapper(e);
			
			printWriter.print(jsonObject);
			printWriter.flush();
			printWriter.close();
		}
	}

	/**
     * 토큰 관련 Exception 발생 시 예외 응답값 구성
     *
     * @param e Exception
     * @return JSONObject
     */
    private JSONObject jsonResponseWrapper(Exception e) {

        String resultMsg = "";
       
        if (e instanceof ExpiredJwtException) {				 	// JWT 토큰 만료
            resultMsg = "TOKEN Expired";
        } else if (e instanceof SignatureException) {			// JWT 허용된 토큰이 아님
            resultMsg = "TOKEN SignatureException Login";
        } else if (e instanceof JwtException) {
            resultMsg = "TOKEN Parsing JwtException";			// JWT 토큰내에서 오류 발생 시
        } else {												// 이외 JTW 토큰내에서 오류 발생
            resultMsg = "OTHER TOKEN ERROR";
        }

        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("status", 401);
        jsonMap.put("code", "9999");
        jsonMap.put("message", resultMsg);
        jsonMap.put("reason", e.getMessage());
        
        JSONObject jsonObject = new JSONObject(jsonMap);
        
        log.error(resultMsg, e);
        return jsonObject;
    }
    
    private String getAccessToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            return authorizationHeader.substring(SecurityConstants.TOKEN_PREFIX.length());
        }

        return null;
    }
}
