package com.okky.restserver.security.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.SignatureException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.web.filter.OncePerRequestFilter;

import com.okky.restserver.security.SecurityConstants;
import com.okky.restserver.security.jwt.TokenUtils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Spring Security 환경설정을 구성하는 단계에서 Filter로 등록한 클래스
 * 지정한 URL 별 JWT 유효성 검증을 수행하며 직접적인 사용자 '인증'을 확인
 * 
 * @author taekwon
 */
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter{

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
			@NonNull FilterChain chain) throws ServletException, IOException {
		// 1. 토큰이 필요하지 않은 API URL에 대해서 배열로 구성합니다.
		List<String> list = Arrays.asList(
				"/test", 
				"/test/user", 
				"/api/token"
		);

		// 2. 토큰이 필요하지 않은 API URL의 경우 => 로직 처리 없이 다음 필터로 이동
		if (list.contains(request.getRequestURI())) {
			chain.doFilter(request, response);
			return;
		}

		// 3. OPTIONS 요청일 경우 => 로직 처리 없이 다음 필터로 이동
		if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
			chain.doFilter(request, response);
			return;
		}

		// [STEP1] Client에서 API를 요청할때 Header를 확인합니다.
		String header = request.getHeader(SecurityConstants.AUTH_HEADER);
		log.debug("{} header Check: " + header);

		try {
			// [STEP2-1] Header 내에 토큰이 존재하는 경우
			if (header != null && !header.equalsIgnoreCase("")) {

				// [STEP2] Header 내에 토큰을 추출합니다.
				String token = TokenUtils.getTokenFromHeader(header);

				// [STEP3] 추출한 토큰이 유효한지 여부를 체크합니다.
				if (TokenUtils.isValidToken(token)) {

					// [STEP4] 토큰을 기반으로 사용자 아이디를 반환 받는 메서드
					String userId = TokenUtils.getUserIdFromToken(token);
					log.debug("{} userId Check: " + userId);

					// [STEP5] 사용자 아이디가 존재하는지 여부 체크
					if (userId != null && !userId.equalsIgnoreCase("")) {

						// TODO: [STEP6] 실제 DB로 조회를 하여 유효한 사용자 인지 확인(인증)하는 부분이 들어가면 될것 같습니다.
						chain.doFilter(request, response);
					} else {
						log.error("TOKEN isn't userId");
					}
					
				} else {	// 토큰이 유효하지 않은 경우
					log.error("TOKEN is invalid");
				}
				
			} else {	// [STEP2-1] 토큰이 존재하지 않는 경우
				log.error("Token is null");
			}
		} catch (Exception e) {
			// Token 내에 Exception이 발생 하였을 경우 => 클라이언트에 응답값을 반환하고 종료합니다.
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
        // JWT 토큰 만료
        if (e instanceof ExpiredJwtException) {
            resultMsg = "TOKEN Expired";
        }
        // JWT 허용된 토큰이 아님
        else if (e instanceof SignatureException) {
            resultMsg = "TOKEN SignatureException Login";
        }
        // JWT 토큰내에서 오류 발생 시
        else if (e instanceof JwtException) {
            resultMsg = "TOKEN Parsing JwtException";
        }
        // 이외 JTW 토큰내에서 오류 발생
        else {
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
}
