package com.okky.restserver.controller;

import java.time.Duration;
import java.util.HashMap;

import org.springframework.http.MediaType;
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
//import com.okky.restserver.service.AuthenticationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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

@RequestMapping(value = "/auth")
@RequiredArgsConstructor
@RestController
@Tag(name = "Authentication", description = "인증")
public class AuthenticationController {
	
	private final JwtProvider jwtProvider;
//	private final AuthenticationService authenticationService;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	
	@Operation(summary = "회원 로그인", description = "1) 로그인을 시도하여 성공시 JWT 발급한다.\n "
													+ "2) JWT 만료 시간 : 10분\n"
													+ "3) Header : x-api-key 값 필요\n"
													+ "4) JWT 만료시 refreshToken을 이용하여 JWT 재발급 필요")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "로그인 성공 : JWT 발급 성공", content = @Content(examples = {
					@ExampleObject(name = "getJwt", 
							summary = "JWT 발급 성공 예시", 
							description = "JWT 만료 시간 10분", 
							value = "{\"status\":200,\"code\":\"S0000\",\"message\":\"SUCCESS\",\"result\":{\"type\":\"object\",\"data\":{\"grantType\":\"Bearer \",\"jwt\":\"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0YWV0YWUiLCJhdXRoIjoidXNlciIsImV4cCI6MTcwNzA2MTk1MH0.q8nmjC9Jxka8nANfv7RbO2q2jtYt03wgV3sN13H_CIZn2t38j7741DCnSr5WDXNablbY9vtfrD8eUso23BZuvA\",\"refreshToken\":\"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0YWV0YWUiLCJhdXRoIjoidXNlciIsImV4cCI6MTcwNzE0Nzc1NX0.zaQI9rOoTcVg9fpE6Mj0n8YsLZIkGJAMX5Wr8cMqYnyWSBfzU86ty4uCAo1t1FUvlokNJyy4hFhkVG-r7whIEg\"}}}") }, 
							mediaType = MediaType.APPLICATION_JSON_VALUE)),
			@ApiResponse(responseCode = "401", description = "인증 실패 : ID/PW 오류로 JWT 발급 불가", content = @Content(examples = {
			          @ExampleObject(name = "",
							summary = "JWT 인증 실패", description = "사용자 인증 실패 ID/PW 오류", 
							value = "{\"status\":401,\"code\":\"E0001\",\"message\":\"Authentication failed.\"}") }, 
							mediaType = MediaType.APPLICATION_JSON_VALUE)),
			@ApiResponse(responseCode = "402", description = "JWT 만료", content = @Content(examples = {
			          @ExampleObject(name = "",
							summary = "JWT 만료", 
							description = "만료된 JWT로 RefreshToken을 이용하여 JWT 갱신 필요", 
							value = "{\"status\":402,\"code\":\"E0002\",\"message\":\"Token has expired.\"}") }, 
							mediaType = MediaType.APPLICATION_JSON_VALUE)),
			@ApiResponse(responseCode = "403", description = "권한 없음", content = @Content(examples = {
			          @ExampleObject(name = "",
							summary = "권한 없음", 
							description = "권한 있는 사용자 인지 확인 필요", 
							value = "{\"status\":403,\"code\":\"E0003\",\"message\":\"Authorization failed.\"}") }, 
							mediaType = MediaType.APPLICATION_JSON_VALUE))
	})
	@io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
				examples = {
						@ExampleObject(name = "성공 Example", value = """ 
		                    { 
		                        "id" : "test01", 
		                        "password" : "1234"
		                    }
								""", description = "id, password 이용하여 서버에서 인증 객체 생성, JWT 발급 성공"),
						@ExampleObject(name = "실패 Example", value = """ 
	                    { 
	                        "id" : "test01", 
	                        "password" : "5678"
	                    }
								""", description = "비밀번호 오류로 인한 실패")
				}
	))
	@PostMapping(value = "/sign-in")
	public Object signIn(
			@RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody SignInDto signInDto) {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				signInDto.getUserId(), signInDto.getPassword());

		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = jwtProvider.generateJwt(authentication, Duration.ofMinutes(10));
		String refreshToken = jwtProvider.generateRefreshToken(authentication, Duration.ofHours(24));
	
		HashMap<String,Object> result = new HashMap<String,Object>();
		result.put("type", "object");
		result.put("data", new JwtDto(SecurityConstants.TOKEN_PREFIX, jwt, refreshToken));
		
		return result;
	}
	
//	@Tag(name = "Authentication", description = "인증")
//	@Operation(summary = "JWT 갱신 (Undeveloped)", description = "RefreshToken을 이용하여 JWT 재발급 (Undeveloped)")
//	@PostMapping("/refresh-token")
//    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(@RequestBody CreateAccessTokenRequest request) {
//        String newAccessToken = authenticationService.createNewAccessToken(request.getRefreshToken());
//
//        return ResponseEntity.status(HttpStatus.CREATED)
//                				.body(new CreateAccessTokenResponse(newAccessToken));
//    }
}
