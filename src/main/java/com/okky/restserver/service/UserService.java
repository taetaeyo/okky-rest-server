package com.okky.restserver.service;

import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.okky.restserver.domain.User;
import com.okky.restserver.dto.UserRequestDto;
import com.okky.restserver.dto.UserResponseDto;
import com.okky.restserver.repository.UserRepository;
import com.okky.restserver.security.exception.DuplicateUserException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	// 사용자 전체 list 조회
	@Transactional(readOnly = true)
	public List<User> getUsersList(){
    	return userRepository.findAll();
    }

	// 사용자 ID로 정보 조회
	@Transactional(readOnly = true)
	public User findById(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException(userId + " 사용자가 존재하지 않습니다."));
    }
	
	// UUID로 회원 정보 조회
	@Transactional(readOnly = true)
	public UserResponseDto findByUuid(UUID uuid) {
		log.info("uuid : {}", uuid);
		
        return UserResponseDto.from(userRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException(uuid + " 사용자가 존재하지 않습니다.")));
    }
	
	// 사용자 추가 (회원 가입) 
	@Transactional
	public UserResponseDto signUp(UserRequestDto userRequestDto) {
		
		if (userRepository.findByUserId(userRequestDto.getUserId()).orElse(null) != null) {
			log.info("		::		Duplicate ID	::");
			throw new DuplicateUserException("이미 가입되어 있는 유저입니다.");
		}
		
		log.info("Create UserId : {}", userRequestDto.getUserId());
		
		User user = User.builder()
						.userId(userRequestDto.getUserId())
						.password(bCryptPasswordEncoder.encode(userRequestDto.getPassword()))
						.email(userRequestDto.getEmail())
						.userName(userRequestDto.getUserName())
						.nickName(userRequestDto.getNickName())
						.build();
		
		return UserResponseDto.from(userRepository.save(user));
	}
	
}