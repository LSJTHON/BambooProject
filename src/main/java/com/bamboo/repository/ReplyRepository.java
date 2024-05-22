package com.bamboo.repository;

import com.bamboo.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    
    //  게시글에서 보여줄 조건의 댓글 목록을 가져오는 쿼리
    @Query("SELECT r FROM Reply r " +
            "WHERE r.isDeleted = false " +
            "OR r.id IN (SELECT rr.reply.id FROM ReReply rr WHERE rr.isDeleted = false)")
    List<Reply> findActiveAndReferencedReplies();
}
