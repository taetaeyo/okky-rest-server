package com.okky.restserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class RestserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestserverApplication.class, args);
	}

}
