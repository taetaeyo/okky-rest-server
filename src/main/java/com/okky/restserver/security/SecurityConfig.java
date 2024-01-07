package com.okky.restserver.security;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.okky.restserver.security.filter.JwtAuthorizationFilter;
import com.okky.restserver.service.UserDetailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Spring Security 환경 설정을 구성하기 위한 클래스
 * 웹 서비스가 로드 될때 Spring Container 의해 관리가 되는 클래스이며 사용자에 대한 ‘인증’과 ‘인가’에 대한 구성을 Bean 메서드로 주입을 한다.
 * 
 * @author taekwon
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig{
	
	private final UserDetailsService userService;

	/**
     * 인증된 사용자가 static 자원의 접근에 대해 ‘인가’에 대한 설정을 담당하는 메서드
     * 
     * @return WebSecurityCustomizer
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // static 자원에 대해서 Security를 적용하지 않음으로 설정
        return web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
    
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
    	log.info("[+] SecurityConfig Start");
    	
    	http
    		// rest api이므로 csrf 보안 미사용
	    	.csrf((csrf) -> csrf.disable()		
			)
	    	// 토큰을 활용하는 경우 모든 요청에 대해 '인가'에 대해서 적용
	        .authorizeHttpRequests((authz) -> authz.anyRequest().permitAll()
    		)
	        // Spring Security JWT Filter Load
	        .addFilterBefore(jwtAuthorizationFilter(), BasicAuthenticationFilter.class)
	        // Session 기반의 인증기반을 사용하지 않고 추후 JWT를 이용하여서 인증 예정
	        .sessionManagement((management) -> management
	        		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)	
    		)
	        // form 기반의 로그인에 대해 비 활성화하며 커스텀으로 구성한 필터를 사용한다.
	        .formLogin((login) -> login	
    				.disable()
    		);

    	return http.build();
    }

	@Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailService userDetailService) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userService)
                .passwordEncoder(bCryptPasswordEncoder)
                .and()
                .build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    /**
     * 9. JWT 토큰을 통하여서 사용자를 인증합니다.
     *
     * @return JwtAuthorizationFilter
     */
    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter();
    }
    
}
