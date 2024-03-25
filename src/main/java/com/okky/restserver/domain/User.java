package com.okky.restserver.domain;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
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

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;
	
//	@GeneratedValue(generator = "uuid4")
//    @GenericGenerator(name = "UUID")
    @Column(name = "uuid", columnDefinition = "BINARY(16)")
	private UUID uuid;
	
	@Column(name = "user_id", nullable = false, unique = true)
	private String userId;
	
	@Column(name = "name", nullable = false)
	private String userName;

	@Column(name = "password", nullable = false)
    private String password;
	
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    
    @Column(name = "nickname", nullable = false, unique = true)
    private String nickName;
    
    @Builder
    public User(Long id, UUID uuid, String userId,String userName, String password, String email, String nickName) {
    	this.id = id;
    	this.uuid = uuid;
    	this.userId = userId;
    	this.userName = userName;
    	this.password = password;
        this.email = email;
        this.nickName = nickName;
    }
    
    /*
     * UUID는 기본키가 아니므로 ID 어노테이션으로 자동 생성할 수가 없으므로
     * @PrePersist 어노테이션을 이용하여 uuid version4 생성
     */
    @PrePersist
    private void beforePersist() {
        if (uuid == null) {
        	uuid = UUID.randomUUID();
        }
    }

    // 권한 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    // 사용자의 id를 반환
    public String getUserId() {
        return userId;
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