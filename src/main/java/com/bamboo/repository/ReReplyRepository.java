package com.bamboo.repository;

import com.bamboo.entity.ReReply;
import com.bamboo.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReReplyRepository extends JpaRepository<ReReply, Long> {

    //  대댓글 존재 여부 확인
    boolean existsByReply(Reply reply);

    //  삭제되지 않은 보여줄 대댓글 가져오기
    @Query("SELECT rr FROM ReReply rr WHERE rr.isDeleted = false")
    List<ReReply> findAllNotDeleted();
}
