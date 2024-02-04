package com.okky.restserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.okky.restserver.repository.UserRepository;

@DataJpaTest
public class JpaUserTest {

	@Autowired
	UserRepository userRepository;
	
}
