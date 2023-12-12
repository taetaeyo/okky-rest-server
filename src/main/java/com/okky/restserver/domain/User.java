package com.okky.restserver.domain;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
public class User implements UserDetails{
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;
	
	@Column(name = "username", nullable = false)
	private String username;

	@Column(name = "password", nullable = false)
    private String password;
	
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    
    @Builder
    public User(Long id,  String username, String password,String email) {
    	this.id = id;
    	this.username = username;
    	this.password = password;
        this.email = email;
    }

    // 권한 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    // 사용자의 id를 반환
    public Long getId() {
        return id;
    }
    
    // 사용자 이름 반환
    @Override
    public String getUsername() {
        return username;
    }

    // 사용자의 패스워드 반환
    @Override
    public String getPassword() {
        return password;
    }

    // 계정 만료 여부 반환
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 잠금 여부 반환
    @Override
    public boolean isAccountNonLocked() {
        return true;	// true : 잠금 되지 않음
    }

    // 패스워드 만료 여부 반환
    @Override
    public boolean isCredentialsNonExpired() {
        return true;	// true : 만료 되지 않음
    }

    // 계정 사용 가능 여부
    @Override
    public boolean isEnabled() {
        return true;	// true : 사용 가능
    }
}