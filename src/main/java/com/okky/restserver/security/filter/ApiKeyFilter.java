package com.okky.restserver.security.filter;

import java.io.IOException;
import java.util.Arrays;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.okky.restserver.aop.AuthenticationErrorCode;
import com.okky.restserver.dto.ErrorDto;
import com.okky.restserver.dto.ResponseCode;
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
		
		// x-api-key 확인 예외 url
		String[] urls = new String[] {"/swagger/", "/swagger-ui/", "/swagger-resources/", "/v3/api-docs"};
		
		if(Arrays.stream(urls).anyMatch(url -> request.getServletPath().contains(url))) {
			log.info("Exclude swagger-related URLs from the ApiKeyFilter");
		} else {
			if (StringUtils.hasText(xApiKey) && apiKey.equals(xApiKey)) {
				log.info("Header x-api-key Check Success");
			} else {
				log.info("Header x-api-key Check {}", xApiKey);

				setJsonResponse(response, ResponseCode.E0000);
				
				return;
			}
		}
		
		filterChain.doFilter(request, response);
		
	}
	
	// 한글 출력을 위해 getWriter() 사용
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
