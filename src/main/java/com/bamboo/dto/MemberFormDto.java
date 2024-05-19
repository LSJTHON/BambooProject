package com.bamboo.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberFormDto {

    private String email;

    private String name; // 유저 아이디

    private String password;
}
