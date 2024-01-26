package com.okky.restserver.service;

import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.okky.restserver.domain.User;
import com.okky.restserver.dto.UserRequestDto;
import com.okky.restserver.dto.UserResponseDto;
import com.okky.restserver.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	// 사용자 전체 list 조회
	public List<User> getUsersList(){
    	return userRepository.findAll();
    }

	// 사용자 ID로 정보 조회
	public User findById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(userId + " 사용자가 존재하지 않습니다."));
    }
	
	// 사용자 추가 (회원 가입) 
	public UserResponseDto signUp(UserRequestDto userRequestDto) {
		// UUID version 4
		String uuid = UUID.randomUUID().toString();
		log.info("Create UUID : {}", uuid);
		log.info("Create UserId : {}", userRequestDto.getId());
		
		User user = User.builder()
						.uuid(uuid)
						.id(userRequestDto.getId())
						.password(bCryptPasswordEncoder.encode(userRequestDto.getPassword()))
						.userName(userRequestDto.getUserName())
						.nickName(userRequestDto.getNickName())
						.build();
		
		return UserResponseDto.from(userRepository.save(user));
	}
}