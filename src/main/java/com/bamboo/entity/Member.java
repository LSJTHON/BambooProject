package com.bamboo.entity;

import com.bamboo.constant.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "member")
@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity implements UserDetails {
    @Column(name = "id",updatable = false)
    private Long id;

    @Id
    @Column(name = "member_email", unique = true)
    private String email;

    @Column(name = "member_name")
    private String name;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "member_is_deleted")
    private boolean isDeleted;

    @Builder
    public Member(String email, String password, String name, Role role){
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
    }


    public void updateDeleted(boolean isDeleted){
        this.isDeleted = isDeleted;
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
}
