package com.okky.restserver;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.okky.restserver.domain.User;
import com.okky.restserver.repository.UserRepository;

@DataJpaTest
public class JpaUserTest {

	@Autowired
	UserRepository userRepository;
	
	@BeforeEach
	void insertTestData() {
		User user = new User();
		user.setUsername("kim ori");
		userRepository.save(user);
		
		user = new User();
		user.setUsername("lee ori");
		userRepository.save(user);
		
		user = new User();
		user.setUsername("kim ental");
		userRepository.save(user);
		
		user = new User();
		user.setUsername("lee ental");
		userRepository.save(user);
		
		user = new User();
		user.setUsername("kim samuel");
		userRepository.save(user);
	}

	@Test
	void findAllTest() { // 저장된 데이터 모두를 Spring JPA에 미리 구현된 findAll 명령을 통해 불러온다
		List<User> userList = userRepository.findAll();
		for(User u : userList) log.info("[FindAll]: " + u.getID() + " | " +u.getUsername());
	}

	@Test
	void find2ByNameTest() { // Like 검색으로 2개만 값을 가져오는 내가 작성한 명령을 실행해본다
		List<User> userList = userRepository.findFirst2ByUsernameLikeOrderByIDDesc("kim%");
		for(User u : userList) log.info("[FindSome]: "  + u.getID() + " | " +u.getUsername());
	}
}
