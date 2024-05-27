package com.bamboo.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddAllowedRequest {

    private Long maxFileCount; //파일의 최대 개수

    private Long maxFileSize; //파일 하나당 최대 업로드 용량

}
