//package com.bamboo.service;
//
//import com.bamboo.constant.Role;
//import com.bamboo.dto.ReplyFormDto;
//import com.bamboo.entity.Board;
//import com.bamboo.entity.Member;
//import com.bamboo.entity.ReReply;
//import com.bamboo.entity.Reply;
//import com.bamboo.repository.BoardRepository;
//import com.bamboo.repository.MemberRepository;
//import com.bamboo.repository.ReReplyRepository;
//import com.bamboo.repository.ReplyRepository;
//import jakarta.persistence.EntityNotFoundException;
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.context.TestPropertySource;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@SpringBootTest
//@Transactional
//@TestPropertySource(locations = "classpath:application-test.properties")
//class ReReplyServiceTest {
//
//    @Autowired
//    MemberRepository memberRepository;
//    @Autowired
//    BoardRepository boardRepository;
//    @Autowired
//    ReplyRepository replyRepository;
//    @Autowired
//    ReReplyRepository reReplyRepository;
//
//    @Autowired
//    ReplyService replyService;
//    @Autowired
//    ReReplyService reReplyService;
//
//    @Autowired
//    PasswordEncoder passwordEncoder;
//
//    public void createMember() {
//        for (int i = 0; i < 3; i++) {
//            Member member = new Member();
//            member.setEmail("test" + i + "@test.com");
//            member.setName("test" + i);
//            String password = passwordEncoder.encode("1234");
//            member.setPassword(password);
//            member.setRole(Role.ADMIN);
//            memberRepository.save(member);
//        }
//    }
//
//    public void createBoard() {
//        this.createMember();
//        for (int i = 0; i < 3; i++) {
//            Board board = new Board();
//            Member member = memberRepository.findById("test" + i + "@test.com")
//                    .orElseThrow(EntityNotFoundException::new);
//            board.setMember(member);
//            board.setTitle("테스트 제목" + i);
//            board.setContent("테스트 내용" + i);
//            board.setDeleted(false);
//            board.setRestored(false);
//            board.setGood(0L);
//            board.setHit(0L);
//            boardRepository.save(board);
//        }
//    }
//
//    public void createReply() {
//        this.createBoard();
//        for (int i = 0; i < 3; i++) {
//            Member member = memberRepository.findById("test" + i + "@test.com")
//                    .orElseThrow(EntityNotFoundException::new);
//            List<Board> board = boardRepository.findByMemberEmail("test" + i + "@test.com");
//
//            ReplyFormDto replyFormDto = new ReplyFormDto();
//            replyFormDto.setContent("테스트 댓글" + i);
//
//            replyService.saveReply(member.getEmail(), board.get(0).getId(), replyFormDto);
//        }
//    }
//
//    @Test
//    @DisplayName("대댓글 등록 테스트")
//    void saveReReplyTest() {
//        this.createReply();
//
//        Member member = memberRepository.findById("test2@test.com")
//                .orElseThrow(EntityNotFoundException::new);
//
//        Board board = boardRepository.findById(1L)
//                .orElseThrow(EntityNotFoundException::new);
//
//        Reply reply = replyRepository.findById(1L)
//                .orElseThrow(EntityNotFoundException::new);
//
//        ReplyFormDto replyFormDto = new ReplyFormDto();
//        replyFormDto.setContent("테스트 대댓글 입니다.");
//
//        Long reReplyId = reReplyService.saveReReply(member.getEmail(), board.getId(), reply.getId(), replyFormDto);
//
//        ReReply reReply = reReplyRepository.findById(reReplyId)
//                .orElseThrow(EntityNotFoundException::new);
//
//        assertEquals(member.getEmail(), reReply.getMember().getEmail());
//        assertEquals(replyFormDto.getContent(), reReply.getContent());
//
//    }
//
//    @Test
//    @DisplayName("대댓글 삭제 테스트")
//    void cancelReReplyTest(){
//        this.createReply();
//
//        Member member = memberRepository.findById("test2@test.com")
//                .orElseThrow(EntityNotFoundException::new);
//
//        Board board = boardRepository.findById(1L)
//                .orElseThrow(EntityNotFoundException::new);
//
//        Reply reply = replyRepository.findById(1L)
//                .orElseThrow(EntityNotFoundException::new);
//
//        ReplyFormDto replyFormDto = new ReplyFormDto();
//        replyFormDto.setContent("테스트 대댓글 입니다.");
//
//        Long reReplyId = reReplyService.saveReReply(member.getEmail(), board.getId(), reply.getId(), replyFormDto);
//
//        ReReply reReply = reReplyRepository.findById(reReplyId)
//                .orElseThrow(EntityNotFoundException::new);
//
//        reReplyService.cancelReReply(reReplyId);
//
//        assertEquals(true, reReply.isDeleted());
//    }
//}