package com.bamboo.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReplyDto {

    private Long id;

    private String email;

    private String name;

    private String content;

    private boolean isDeleted;

    private LocalDateTime regTime;
}
