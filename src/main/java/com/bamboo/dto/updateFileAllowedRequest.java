package com.bamboo.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class updateFileAllowedRequest {

    private Long maxFileCount; //파일의 최대 개수

    private Long maxFileSize; //파일 하나당 최대 업로드 용량

    private List<String> extensions; //파일 확장자 제한
}
