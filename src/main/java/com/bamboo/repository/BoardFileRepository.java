package com.bamboo.repository;

import com.bamboo.entity.Board;
import com.bamboo.entity.BoardFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardFileRepository extends JpaRepository<BoardFile, Long> {
    List<BoardFile> findAllByBoardIdOrderByIdAsc(Long boardId);

    List<BoardFile> findByBoardId(Long boardId);

    void deleteByBoard(Board board); // 추가된 메서드
}
