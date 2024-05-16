package com.codehows.sbd.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;
import java.util.List;

@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",updatable = false)
    private Long id;


    //unique 중복 불가
    @Column(name ="email", nullable = false, unique = true)
    private String email;

    @Column(name = "password")
    private String password;


    //google api 에서 사용할 닉네임 추가
    @Column(name = "nickname",unique = true)
    private String nickname;


    //google api 를 추가하고 nickname을 생성자에 추가 했음
    @Builder
    public User(String email, String password, String nickname){
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    @Override //사용자의 이메일(아이디) 반환
    public String getUsername(){
        return email;
    }
    @Override //사용자의 패스워드 반환
    public String getPassword(){
        return this.password;
    }

    @Override //계정 만료 여부 반환
    public boolean isAccountNonExpired(){
        //만료되었는지 확인하는 로직
        return true; // true 만료되지 않았음
    }

    @Override
    public boolean isAccountNonLocked(){
        //계정 잠금되었는지 확인하는 로직
        return true; //true -> 잠금되지 않았음
    }

    @Override //패스워드의 만료 여부 반환
    public boolean isCredentialsNonExpired(){
        //패스워드가 만료되었는지 확인하는 로직
        return true;  //true -> 만료되지 않았음
    }

    @Override //계정 사용 가능 여부 반환
    public boolean isEnabled(){
        //계정이 사용 가능한지 확인하는 로직
        return true; //true -> 사용 가능
    }

    //사용자 이름 변경
    public User update(String nickname){
        this.nickname = nickname;

        return this;
    }
}
