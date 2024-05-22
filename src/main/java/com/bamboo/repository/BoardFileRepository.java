package com.bamboo.repository;

import com.bamboo.entity.BoardFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardFileRepository extends JpaRepository<BoardFile, Long> {
    List<BoardFile> findAllByBoardIdOrderByIdAsc(Long boardId);
}
