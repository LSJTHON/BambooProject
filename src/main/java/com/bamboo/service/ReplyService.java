package com.bamboo.service;

import com.bamboo.dto.ReplyDto;
import com.bamboo.dto.ReplyFormDto;
import com.bamboo.entity.Board;
import com.bamboo.entity.Member;
import com.bamboo.entity.Reply;
import com.bamboo.repository.BoardRepository;
import com.bamboo.repository.MemberRepository;
import com.bamboo.repository.ReReplyRepository;
import com.bamboo.repository.ReplyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
//@Transactional
@RequiredArgsConstructor
public class ReplyService {

    private final MemberRepository memberRepository;

    private final BoardRepository boardRepository;

    private final ReplyRepository replyRepository;

    private final ReReplyRepository reReplyRepository;

//    테스트용 코드
    private final PasswordEncoder passwordEncoder;

    //  댓글 저장
    public Long saveReply(String email, Long boardId, ReplyFormDto replyFormDto){
        //  테스트용 코드
//        Member member1 = new Member();
//        member1.setEmail(email);
//        member1.setName("홍길동");
//        String password = passwordEncoder.encode("1234");
//        member1.setPassword(password);
//        member1.setRole(Role.ADMIN);
//        memberRepository.save(member1);
//        Board board1 = new Board();
//        board1.setMember(member1);
//        board1.setTitle("테스트 제목");
//        board1.setContent("테스트 내용");
//        board1.setDeleted(false);
//        board1.setRestored(false);
//        board1.setGood(0L);
//        board1.setHit(0L);
//        boardRepository.save(board1);
//        System.out.println(board1.getId());

        Reply reply = replyFormDto.createReply();
        // 디버깅 로그 출력
//        System.out.println("Before setting member and board:");
//        System.out.println("Reply ID: " + reply.getId());
//        System.out.println("Reply Content: " + reply.getContent());
//        System.out.println("Reply Member: " + reply.getMember());
//        System.out.println("Reply Board: " + reply.getBoard());

        reply.setDeleted(false);

        Member member = memberRepository.findById(email)
                .orElseThrow(EntityNotFoundException::new);
        reply.setMember(member);

        Board board = boardRepository.findById(boardId)
                .orElseThrow(EntityNotFoundException::new);
        reply.setBoard(board);

        // 디버깅 로그 출력
//        System.out.println("After setting member and board:");
//        System.out.println("Reply ID: " + reply.getId());
//        System.out.println("Reply Content: " + reply.getContent());
//        System.out.println("Reply Member: " + reply.getMember().getEmail());
//        System.out.println("Reply Board: " + reply.getBoard().getId());

        replyRepository.save(reply);

        // 디버깅 로그 출력
//        System.out.println("After saving to repository:");
//        System.out.println("Reply ID: " + reply.getId());
//        System.out.println("Reply Content: " + reply.getContent());
//        System.out.println("Reply RegTime: " + reply.getRegTime());

        return reply.getId();
    }

    //  댓글 삭제
    public void cancelReply(Long replyId){
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(EntityNotFoundException::new);
        reply.setDeleted(true);
        replyRepository.save(reply);
    }

    //  댓글이 삭제 && 대댓글 보유 여부 확인
    public boolean isDeletedAndHasReReply(Long replyId){
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(EntityNotFoundException::new);

        //  댓글이 삭제되었다면
        if (reply.isDeleted()) {
            // 대댓글 테이블에서 사용중이라면
            return reReplyRepository.existsByReply(reply);
        } else {
            // 삭제되지 않았다면
            return false;
        }
    }

    //  댓글 목록 가져오기
    public List<ReplyDto> getReplyList(Long boardId){
        List<Reply> replies = replyRepository.findActiveAndReferencedReplies(boardId);

        // 디버깅 로그: 조회된 댓글 개수 출력
//        System.out.println("Number of replies retrieved: " + replies.size());

        List<ReplyDto> replyDtos = new ArrayList<>();

        for (Reply reply : replies){
            // 디버깅 로그: 각 댓글 정보 출력
//            System.out.println("Reply ID: " + reply.getId());
//            System.out.println("Reply Content: " + reply.getContent());
//            System.out.println("Reply Member: " + reply.getMember().getEmail());
//            System.out.println("Reply Board: " + reply.getBoard().getId());

            ReplyDto replyDto = new ReplyDto();
            replyDto.setId(reply.getId());
            replyDto.setEmail(reply.getMember().getEmail());
            replyDto.setName(reply.getMember().getName());
            replyDto.setContent(reply.getContent());
            replyDto.setDeleted(reply.isDeleted());
            replyDto.setRegTime(reply.getRegTime());

            replyDtos.add(replyDto);
        }

        return replyDtos;
    }
}
