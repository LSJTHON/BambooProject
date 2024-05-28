package com.bamboo.repository;

import com.bamboo.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    //  게시글에서 보여줄 조건의 댓글 목록을 가져오는 쿼리
    @Query("SELECT r FROM Reply r " +
            "WHERE r.board.id = :boardId AND (r.isDeleted = false " +
            "OR r.id IN (SELECT rr.reply.id FROM ReReply rr WHERE rr.isDeleted = false))")
    List<Reply> findActiveAndReferencedReplies(@Param("boardId") Long boardId);

    List<Reply> findByRegTimeBetween(LocalDateTime startDate, LocalDateTime endDate);


    //사용자의 게시물 정지상태 복구
    @Modifying
    @Transactional
    @Query(value = "UPDATE reply SET reply_is_deleted = 0 WHERE member_email = :email", nativeQuery = true)
    void restoredReply(@Param("email") String email);

    //사용자의 게시물 정지
    @Modifying
    @Transactional
    @Query(value = "UPDATE reply SET reply_is_deleted = 1 WHERE member_email = :email", nativeQuery = true)
    void deletedReply(@Param("email") String email);
}
