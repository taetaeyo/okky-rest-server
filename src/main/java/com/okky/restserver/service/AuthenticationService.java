//package com.okky.restserver.service;
//
//import java.time.Duration;
//
//import org.springframework.stereotype.Service;
//
//import com.okky.restserver.domain.User;
//import com.okky.restserver.dto.SignInDto;
//import com.okky.restserver.security.jwt.JwtProvider;
//
//import lombok.RequiredArgsConstructor;
//
//@Service
//@RequiredArgsConstructor
//public class AuthenticationService {
//	
//	private final JwtProvider jwtProvider;
//	private final RefreshTokenService refreshTokenService;
//    private final UserService userService;
//	
//	public String createNewAccessToken(String refreshToken) {
//        // 토큰 유효성 검사에 실패하면 예외 발생
//        if(!jwtProvider.validationToken(refreshToken)) {
//            throw new IllegalArgumentException("Unexpected token");
//        }
//
//        String userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
//        User user = userService.findById(userId);
//
////        return jwtProvider.generateToken(user, Duration.ofHours(2));
//        return "Undeveloped";
//    }
//}
