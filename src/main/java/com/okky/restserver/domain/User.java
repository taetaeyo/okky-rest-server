package com.okky.restserver.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class User {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

	@Column(name = "password", nullable = false)
    private String password;
	
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    
    @Builder
    public User(Long id, String password, String email) {
    	this.id = id;
    	this.password = password;
        this.email = email;
    }
}