package com.okky.restserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.okky.restserver.service.UserDetailService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig{
	
	private final UserDetailsService userService;

    @Bean
    WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers("/static/**");
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	http
    		.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests		//페이지 권한 설정
	        .requestMatchers(
	        			"/",
		        		"/error",
	                    "/api/**",
	                    "/static/**",
	                    "/auth/**",
	                    "/test/**"
    		).permitAll()	 				//securityFilter 제외 url
	        .anyRequest().authenticated() 	//permitAll외 모든 경로는 인증이 필요
    		)
    		//로그인 설정
            .formLogin(login -> login
                    .loginPage("/auth/loginForm")
                    .failureUrl("/auth/preout")
                    .permitAll())
          //로그아웃 설정
            .logout(logout -> logout
                    .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout"))
                    .logoutSuccessHandler(logoutSuccessHandler())
            		.addLogoutHandler(new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter(
            				ClearSiteDataHeaderWriter.Directive.CACHE,
            	            ClearSiteDataHeaderWriter.Directive.COOKIES,
            	            ClearSiteDataHeaderWriter.Directive.STORAGE)))
            		.invalidateHttpSession(true)
                    .clearAuthentication(true))
            //기본 예외설정
//            .exceptionHandling(handling -> handling
//                    .accessDeniedHandler(accessDeniedHandler()) //권한 접근 예외(Role)
//                    .authenticationEntryPoint(enrtyPointHandler()))
            .headers(headers -> headers
            		.frameOptions((frameOptions) -> frameOptions
            				.sameOrigin()
            				.contentSecurityPolicy((contentSecurityPolicy) -> contentSecurityPolicy
            						//.policyDirectives("default-src 'self'") //if required the full CSP(Contents Security Policy)
            						.policyDirectives("form-action 'self';")
            				)
            			)
            		)
            .securityContext((securityContext) -> securityContext
            		.securityContextRepository(
            				new DelegatingSecurityContextRepository(
            						new RequestAttributeSecurityContextRepository(),
            						new HttpSessionSecurityContextRepository())
            		)
            		.securityContextRepository(new HttpSessionSecurityContextRepository()) // Spring security 6
            ) 
            .sessionManagement(management -> management
                    .maximumSessions(1)
                    .expiredUrl("/auth/preout")
                    .sessionRegistry(sessionRegistry()));
    		
		http.csrf().disable();
		
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
    
    @Bean
    SpringSessionBackedSessionRegistry<? extends Session> sessionRegistry() {
        return new SpringSessionBackedSessionRegistry<>(this.sessionRepository);
    }
}
