package com.bamboo.repository;

import com.bamboo.entity.ReReply;
import com.bamboo.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface ReReplyRepository extends JpaRepository<ReReply, Long> {

    //  대댓글 존재 여부 확인
    boolean existsByReply(Reply reply);

    // 삭제되지 않은 대댓글 가져오기
    @Query("SELECT rr FROM ReReply rr WHERE rr.isDeleted = false AND rr.reply.board.id = :boardId")
    List<ReReply> findAllNotDeleted(@Param("boardId") Long boardId);


    List<ReReply> findByRegTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

    //사용자의 게시물 정지상태 복구
    @Modifying
    @Transactional
    @Query(value = "UPDATE re_reply SET re_reply_is_deleted = 0 WHERE member_email = :email", nativeQuery = true)
    void restoredRe_Reply(@Param("email") String email);

    //사용자의 게시물 정지
    @Modifying
    @Transactional
    @Query(value = "UPDATE re_reply SET re_reply_is_deleted = 1 WHERE member_email = :email", nativeQuery = true)
    void deletedRe_Reply(@Param("email") String email);
}


