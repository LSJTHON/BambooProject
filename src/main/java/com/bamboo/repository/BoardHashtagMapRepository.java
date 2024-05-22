package com.bamboo.repository;

import com.bamboo.entity.BoardHashtagMap;
import com.bamboo.entity.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardHashtagMapRepository extends JpaRepository<BoardHashtagMap, Long> {

    @Query("SELECT bhm.hashtag FROM BoardHashtagMap bhm WHERE bhm.board.id = :boardId")
    List<Hashtag> findHashtagsByBoardId(@Param("boardId") Long boardId);
}
