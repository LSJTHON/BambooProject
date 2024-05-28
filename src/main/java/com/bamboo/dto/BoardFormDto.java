package com.bamboo.dto;

import com.bamboo.entity.Board;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BoardFormDto {

    private Long id;

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    private String hashtag;

    private boolean isDeleted;

    private List<BoardFileDto> boardFileDtoList = new ArrayList<>(); //파일 저장 리스트

    private List<Long> boardFileIds = new ArrayList<>(); //파일 이름 저장 리스트

    private List<HashtagDto> hashtagDtoList = new ArrayList<>(); //태그 이름 저장 리스트

    private static ModelMapper modelMapper = new ModelMapper();

    private Long hit = 0L;

    private Long good = 0L;

    public Board createBoard() {
        Board board =  modelMapper.map(this, Board.class);
        board.setDeleted(false);

        return board;
    }

    public static BoardFormDto of(Board board) {
        return modelMapper.map(board, BoardFormDto.class);
    }

}
