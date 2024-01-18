package com.okky.restserver.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import com.okky.restserver.security.filter.ApiKeyFilter;
import com.okky.restserver.security.filter.JwtAuthorizationFilter;
import com.okky.restserver.security.jwt.JwtAccessDeniedHandler;
import com.okky.restserver.security.jwt.JwtAuthenticationEntryPoint;
import com.okky.restserver.security.jwt.JwtProvider;
import com.okky.restserver.security.jwt.JwtSecurityConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Spring Security 환경 설정을 구성하기 위한 클래스
 * 웹 서비스가 로드 될때 Spring Container 의해 관리가 되는 클래스이며 사용자에 대한 ‘인증’과 ‘인가’에 대한 구성을 Bean 메서드로 주입을 한다.
 * 
 * @author taekwon
 */
@Slf4j
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final JwtProvider jwtProvider;
	private final CorsFilter corsFilter;
	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final ApiKeyFilter apiKeyFilter;

    /**
     * HTTP에 대해서 ‘인증’과 ‘인가’를 담당하는 메서드
     * 필터를 통해 인증 방식과 인증 절차에 대해서 등록하며 설정을 담당하는 메서드
     * 
     * @param http HttpSecurity
     * @return SecurityFilterChain
     * @throws Exception Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	
    	http
    		// token을 사용하는 방식이기 때문에 csrf를 disable
	    	.csrf((csrf) -> csrf.disable()		
			)
	    	 // Spring Security JWT Filter Load
	    	.addFilter(corsFilter)
	    	.addFilterBefore(apiKeyFilter, UsernamePasswordAuthenticationFilter.class)
	        .addFilterBefore(new JwtAuthorizationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)
	        .exceptionHandling(exceptionHandling -> exceptionHandling
	                .accessDeniedHandler(jwtAccessDeniedHandler)
	                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            )
	    	// token을 활용하는 경우 모든 요청에 대해 '인가'에 대해서 적용
	    	.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
	                .requestMatchers("/test",
	                				"/auth/**",
	                				"/api/all/**",
	                				"/swagger/**",
	                				"/swagger-ui/**",
	                				"/swagger-resources/**",
	                				"/v3/api-docs/**").permitAll()
	                .anyRequest().authenticated()
            )
	        // Session 기반의 인증기반을 사용하지 않고 추후 JWT를 이용하여서 인증 예정
	        .sessionManagement((management) -> management
	        		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)	
    		)
	        // form 기반의 로그인에 대해 비 활성화하며 커스텀으로 구성한 필터를 사용한다.
	        .formLogin((login) -> login	
    				.disable()
    		)
	        .with(new JwtSecurityConfig(jwtProvider), customizer -> {});

    	return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
}
