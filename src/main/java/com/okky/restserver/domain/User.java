package com.okky.restserver.domain;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "USERS")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
@Entity
public class User implements UserDetails{
	
	private static final long serialVersionUID = 1L;

	@Schema(name = "UUID")
	@Id
	@GeneratedValue(generator = "uuid4")
    @GenericGenerator(name = "UUID")
    @Column(name = "uuid", columnDefinition = "BINARY(16)")
	private UUID uuid;
	
	@Schema(name = "사용자 ID")
    @Column(name = "id", nullable = false, unique = true)
    private String id;
	
	@Schema(name = "실명")
	@Column(name = "name", nullable = false)
	private String userName;

	@Schema(name = "사용자 비밀번호")
	@Column(name = "password", nullable = false)
    private String password;
	
	@Schema(name = "이메일")
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    
    @Schema(name = "닉네임")
    @Column(name = "nickname", nullable = false, unique = true)
    private String nickName;
    
    @Builder
    public User(UUID uuid, String id,  String userName, String password, String email, String nickName) {
    	this.uuid = uuid;
    	this.id = id;
    	this.userName = userName;
    	this.password = password;
        this.email = email;
        this.nickName = nickName;
    }

    // 권한 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    // 사용자의 id를 반환
    public String getId() {
        return id;
    }
    
    // 사용자 이름 반환
    @Override
    public String getUsername() {
        return userName;
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