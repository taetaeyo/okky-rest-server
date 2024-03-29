package com.okky.restserver.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "refresh_token")
@Getter
@Entity
public class RefreshToken {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, unique = true)
	private Long id;
	
	@Column(name = "user_seq", nullable = false, unique = true)
	private Long userSeq;
	
	@Column(name = "refresh_token", nullable = false)
	private String refreshToken;
	
	public RefreshToken(Long userSeq, String refreshToken) {
	    this.userSeq = userSeq;
	    this.refreshToken = refreshToken;
	}
	
	public RefreshToken update(String newRefreshToken) {
	    this.refreshToken = newRefreshToken;
	
	    return this;
	}
}
