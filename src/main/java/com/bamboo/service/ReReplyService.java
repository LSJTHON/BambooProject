package com.bamboo.service;

import com.bamboo.dto.ReReplyDto;
import com.bamboo.dto.ReplyFormDto;
import com.bamboo.entity.Board;
import com.bamboo.entity.Member;
import com.bamboo.entity.ReReply;
import com.bamboo.entity.Reply;
import com.bamboo.repository.BoardRepository;
import com.bamboo.repository.MemberRepository;
import com.bamboo.repository.ReReplyRepository;
import com.bamboo.repository.ReplyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReReplyService {

    private final MemberRepository memberRepository;

    private final BoardRepository boardRepository;

    private final ReplyRepository replyRepository;

    private final ReReplyRepository reReplyRepository;

    //  대댓글 저장
    public Long saveReReply(String email, Long boardId, ReplyFormDto replyFormDto){
        ReReply reReply = replyFormDto.createReReply();

        Member member = memberRepository.findById(email)
                .orElseThrow(EntityNotFoundException::new);
        reReply.setMember(member);

        Board board = boardRepository.findById(boardId)
                .orElseThrow(EntityNotFoundException::new);
        reReply.setBoard(board);

        Reply reply = replyRepository.findById(replyFormDto.getReplyId())
                .orElseThrow(EntityNotFoundException::new);
        reReply.setReply(reply);

        reReplyRepository.save(reReply);

        return reReply.getId();
    }

    //  대댓글 삭제
    public void cancelReReply(Long reReplyId){
        ReReply reReply = reReplyRepository.findById(reReplyId)
                .orElseThrow(EntityNotFoundException::new);
        reReply.setDeleted(true);
        reReplyRepository.save(reReply);
    }

    //  대댓글 목록 가져오기
    public List<ReReplyDto> getReReplyList(Long boardId){
        List<ReReply> reReplies = reReplyRepository.findAllNotDeleted();

        // 디버깅 로그: 조회된 댓글 개수 출력
//        System.out.println("Number of reReplies retrieved: " + reReplies.size());

        List<ReReplyDto> replyDtos = new ArrayList<>();

        for (ReReply reReply : reReplies){
            // 디버깅 로그: 각 댓글 정보 출력
//            System.out.println("ReReply ID: " + reReply.getId());
//            System.out.println("ReReply Content: " + reReply.getContent());
//            System.out.println("ReReply Member: " + reReply.getMember().getEmail());
//            System.out.println("ReReply Board: " + reReply.getBoard().getId());
//            System.out.println("ReReply ReplyId: " + reReply.getReply().getId());

            ReReplyDto reReplyDto = new ReReplyDto();
            reReplyDto.setId(reReply.getId());
            reReplyDto.setEmail(reReply.getMember().getEmail());
            reReplyDto.setName(reReply.getMember().getName());
            reReplyDto.setContent(reReply.getContent());
            reReplyDto.setDeleted(reReply.isDeleted());
            reReplyDto.setRegTime(reReply.getRegTime());
            reReplyDto.setParentsId(reReply.getReply().getId());

            replyDtos.add(reReplyDto);
        }

        return replyDtos;
    }

}
