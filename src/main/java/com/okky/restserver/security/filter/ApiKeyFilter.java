package com.okky.restserver.security.filter;

import java.io.IOException;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.okky.restserver.aop.AuthenticationErrorCode;
import com.okky.restserver.security.SecurityConstants;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 모든 HTTP Header에서 x-api-key 값 check
 * @author taekwon
 *
 */
@Slf4j
@Component
public class ApiKeyFilter extends OncePerRequestFilter {

	@Value("${api.key}")
	private String apiKey;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		// Header Check
		String xApiKey = request.getHeader(SecurityConstants.API_KEY_AUTH_HEADER_NAME);
		log.info("Header x-api-key Check {}", xApiKey);
		
		if (StringUtils.hasText(xApiKey)) {
			if (apiKey.equals(xApiKey)) {
				log.info("Header x-api-key Check Success");
				filterChain.doFilter(request, response);
			}
		} else {
			setJsonResponse(response, AuthenticationErrorCode.X_API_KEY_ERROR);
		    return;
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
