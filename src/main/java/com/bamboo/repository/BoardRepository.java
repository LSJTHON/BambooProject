package com.bamboo.repository;

import com.bamboo.entity.Board;
import com.bamboo.entity.Reply;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>,
        QuerydslPredicateExecutor<Board>, BoardRepositoryCustom {

    //  사용자 이름으로 글 목록 조회 쿼리
    List<Board> findByMemberEmail(String memberEmail);

    //  글 제목으로 글 목록 조회 쿼리
    List<Board> findByTitle(String title);

    // 글 본문으로 글 목록 조회 쿼리
    List<Board> findByContent(String content);

    List<Board> findAllByOrderByRegTimeDesc();
    List<Board> findAllByOrderByTitleDesc();
    List<Board> findAllByOrderByMemberNameDesc();

    List<Board> findByRegTimeBetween(LocalDateTime startDate, LocalDateTime endDate);


    //사용자의 게시물 정지상태 복구
    @Modifying
    @Transactional
    @Query(value = "UPDATE board SET board_is_deleted = 0 WHERE member_email = :email", nativeQuery = true)
    void restoredBoard(@Param("email") String email);

    //사용자의 게시물 정지
    @Modifying
    @Transactional
    @Query(value = "UPDATE board SET board_is_deleted = 1 WHERE member_email = :email", nativeQuery = true)
    void deletedBoard(@Param("email") String email);
}
