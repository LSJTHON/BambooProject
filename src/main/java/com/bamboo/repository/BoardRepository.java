package com.bamboo.repository;

import com.bamboo.entity.Board;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

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


}
