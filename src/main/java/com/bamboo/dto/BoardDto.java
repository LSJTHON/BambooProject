package com.bamboo.dto;

import com.bamboo.entity.Board;
import com.bamboo.entity.Hashtag;
import com.bamboo.entity.Member;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BoardDto {

    private Long id;

    private String title;

    private Member member;

    private LocalDateTime regTime;

    private Long hit = 0L;

    private String content;

    private List<BoardFileDto> boardFileDtoList = new ArrayList<>();

    private List<Long> boardFileIds = new ArrayList<>();

    private List<String> hashtags;

    private Long good = 0L;

    private boolean isDeleted;

    private static ModelMapper modelMapper = new ModelMapper();

    public Board createBoard() {
        return modelMapper.map(this, Board.class);
    }

    public static BoardDto of(Board board){
        return modelMapper.map(board, BoardDto.class);
    }
}
