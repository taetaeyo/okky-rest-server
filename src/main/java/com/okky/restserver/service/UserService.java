package com.okky.restserver.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.okky.restserver.domain.User;
import com.okky.restserver.dto.AddUserRequestDto;
import com.okky.restserver.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	
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
	public String save(AddUserRequestDto userDto) {
		
		return userRepository.save(User.builder()
					.uuid(null)
					.id(null)
					.password(null)
					.userName(null)
					.nickname(null)
					.build()).getUuid();
	}
}