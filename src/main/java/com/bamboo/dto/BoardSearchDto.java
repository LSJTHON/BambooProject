package com.bamboo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardSearchDto {

    // 검색 유형 선택 (제목, 본문, 작성자 이름, 해시태그)
    private String SearchBy;

    // 조회할 검색어 저장 변수
    private String SearchQuery = "";
}
