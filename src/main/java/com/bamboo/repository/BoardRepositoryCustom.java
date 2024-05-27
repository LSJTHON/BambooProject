package com.bamboo.repository;

import com.bamboo.dto.BoardSearchDto;
import com.bamboo.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {

    Page<Board> getBoardPage(BoardSearchDto boardSearchDto, Pageable pageable);

    Page<Board> getSortedBoardPage(BoardSearchDto boardSearchDto, Pageable pageable, String sort);

    Page<Board> getDeletedBoardPage(BoardSearchDto boardSearchDto, Pageable pageable);
}
