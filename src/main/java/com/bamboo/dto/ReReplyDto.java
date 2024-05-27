package com.bamboo.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReReplyDto {

    private Long id;

    private Long parentsId;

    private String email;

    private String name;

    private String content;

    private boolean isDeleted;

    private LocalDateTime regTime;
}
