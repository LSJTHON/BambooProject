package com.bamboo.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDeleteDto {
    private String email;
    private boolean isDeleted;
}
