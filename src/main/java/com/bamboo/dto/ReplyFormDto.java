package com.bamboo.dto;

import com.bamboo.entity.ReReply;
import com.bamboo.entity.Reply;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class ReplyFormDto {

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    // 대댓글의 경우 부모 댓글 ID 가짐
    private Long replyId;

    // 댓글 객체 생성
    public Reply createReply() {
        Reply reply = new Reply();
        reply.setContent(this.content);
        return reply;
    }

    // 대댓글 객체 생성
    public ReReply createReReply() {
        ReReply reReply = new ReReply();
        reReply.setContent(this.content);
        return reReply;
    }
}
