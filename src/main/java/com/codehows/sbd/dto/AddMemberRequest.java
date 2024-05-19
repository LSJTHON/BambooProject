package com.codehows.sbd.dto;

import com.codehows.sbd.constant.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddMemberRequest {
    private String email;
    private String name;
    private String password;
    private Role role;
}
