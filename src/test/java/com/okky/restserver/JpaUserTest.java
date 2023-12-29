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
	
}
