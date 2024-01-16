package com.okky.restserver.controller;

import java.time.Duration;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.okky.restserver.dto.JwtDto;
import com.okky.restserver.dto.SignInDto;
import com.okky.restserver.security.SecurityConstants;
import com.okky.restserver.security.jwt.JwtProvider;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Authentication
 * @author taekwon
 *
 */

@Slf4j
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
@RestController
public class AuthenticationController {
	
	private final JwtProvider jwtProvider;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	
	@Tag(name = "Authentication", description = "인증")
	@Operation(summary = "사용자 로그인", description = "로그인을 시도하여 성공시 JWT 발급한다. JWT 만료 시간은 10분")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "로그인 성공 : JWT 발급 성공", content = @Content(examples = {
	          @ExampleObject(name = "getUserAttribute",
                      summary = "JWT 발급 성공 예시",
                      description = "JWT 만료 시간 10분",
                      value = "{\"authType\":\"Bearer \",\"jwt\":\"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0MDEiLCJhdXRoIjoidXNlciIsImV4cCI6MTcwNTM4NzY4OX0.qkgb-IZXHCcqmb6xxgL2IP2GLHjjazyDofk3ReOoBQGIutHshkSZ3U3NEaNrRZvJ6QBXQf-WlysZ3YhD5r2y0A\"}")},
								mediaType = MediaType.APPLICATION_JSON_VALUE)),
			@ApiResponse(responseCode = "401", description = "인증 실패 : ID/PW 오류로 JWT 발급 불가") })
	@io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
				examples = {
						@ExampleObject(name = "example01", value = """ 
		                    { 
		                        "id" : "test01", 
		                        "password" : "1234"
		                    }
								""", description = "성공 Example"),
						@ExampleObject(name = "example02", value = """ 
	                    { 
	                        "id" : "test01", 
	                        "password" : "5678"
	                    }
								""", description = "실패 Example")
				}
			))
	@PostMapping(value = "/sign-in")
	public ResponseEntity<JwtDto> signIn(@RequestBody 
										@io.swagger.v3.oas.annotations.parameters.RequestBody SignInDto signInDto) {
		UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(signInDto.getId(), signInDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        String jwt = jwtProvider.generateToken(authentication, Duration.ofMinutes(10));
		
		return ResponseEntity.status(HttpStatus.OK)
								.body(new JwtDto(SecurityConstants.TOKEN_PREFIX, jwt));
	}
	
//	@PostMapping("/refresh-token")
//    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(@RequestBody CreateAccessTokenRequest request) {
//        String newAccessToken = authenticationService.createNewAccessToken(request.getRefreshToken());
//
//        return ResponseEntity.status(HttpStatus.CREATED)
//                				.body(new CreateAccessTokenResponse(newAccessToken));
//    }
}
