//package com.bamboo.service;
//
//import com.bamboo.constant.Role;
//import com.bamboo.dto.ReplyFormDto;
//import com.bamboo.entity.Board;
//import com.bamboo.entity.Member;
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
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.TestPropertySource;
//
//import static org.junit.jupiter.api.Assertions.*;
//@SpringBootTest
//@Transactional
//@TestPropertySource(locations = "classpath:application-test.properties")
//class ReplyServiceTest {
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
//    public void createMember(){
//        for (int i = 0; i < 3; i++) {
//            Member member = new Member();
//            member.setEmail("test"+i+"@test.com");
//            member.setName("test"+i);
//            String password = passwordEncoder.encode("1234");
//            member.setPassword(password);
//            member.setRole(Role.ADMIN);
//            memberRepository.save(member);
//        }
//    }
//
//    public void createBoard(){
//        this.createMember();
//        for (int i = 0; i < 3; i++) {
//            Board board = new Board();
//            Member member = memberRepository.findById("test"+i+"@test.com")
//                    .orElseThrow(EntityNotFoundException::new);
//            board.setMember(member);
//            board.setTitle("테스트 제목"+i);
//            board.setContent("테스트 내용"+i);
//            board.setDeleted(false);
//            board.setRestored(false);
//            board.setGood(0L);
//            board.setHit(0L);
//            boardRepository.save(board);
//        }
//    }
//
//    @Test
//    @DisplayName("댓글 등록 테스트")
//    void saveReplyTest(){
//        this.createBoard();
//
//        Member member = memberRepository.findById("test2@test.com")
//                .orElseThrow(EntityNotFoundException::new);
//
//        Board board = boardRepository.findById(1L)
//                .orElseThrow(EntityNotFoundException::new);
//
//        ReplyFormDto replyFormDto = new ReplyFormDto();
//        replyFormDto.setContent("테스트 댓글 입니다.");
//
//        Long replyId = replyService.saveReply(member.getEmail(), board.getId(), replyFormDto);
//
//        Reply reply = replyRepository.findById(replyId)
//                .orElseThrow(EntityNotFoundException::new);
//
//        assertEquals(member.getEmail(), reply.getMember().getEmail());
//        assertEquals(replyFormDto.getContent(), reply.getContent());
//
//    }
//
//    @Test
//    @DisplayName("댓글 삭제 테스트")
//    void deleteReplyTest(){
//        this.createBoard();
//
//        Member member = memberRepository.findById("test2@test.com")
//                .orElseThrow(EntityNotFoundException::new);
//
//        Board board = boardRepository.findById(1L)
//                .orElseThrow(EntityNotFoundException::new);
//
//        ReplyFormDto replyFormDto = new ReplyFormDto();
//        replyFormDto.setContent("테스트 댓글 입니다.");
//
//        Long replyId = replyService.saveReply(member.getEmail(), board.getId(), replyFormDto);
//
//        System.out.println(replyRepository.findById(replyId).get().isDeleted());
//
//        replyService.cancelReply(replyId);
//
//        System.out.println(replyRepository.findById(replyId).get().isDeleted());
//
//    }
//
//    @Test
//    @DisplayName("대댓글 보유 여부 테스트")
//    void isDeletedAndHasReReplyTest(){
//        this.createBoard();
//
//        Member member = memberRepository.findById("test2@test.com")
//                .orElseThrow(EntityNotFoundException::new);
//        Board board = boardRepository.findById(1L)
//                .orElseThrow(EntityNotFoundException::new);
//        ReplyFormDto replyFormDto = new ReplyFormDto();
//        replyFormDto.setContent("테스트 댓글 입니다.");
//
//        Long replyId = replyService.saveReply(member.getEmail(), board.getId(), replyFormDto);
//
//        System.out.println("대댓글 없을 경우: "+replyService.isDeletedAndHasReReply(replyId));
//
//        ReplyFormDto replyFormDto2 = new ReplyFormDto();
//        replyFormDto2.setContent("테스트 대댓글 입니다.");
//
//        reReplyService.saveReReply(member.getEmail(), board.getId(), replyId, replyFormDto2);
//
//        replyService.cancelReply(replyId);
//        System.out.println("대댓글 있을 경우: "+replyService.isDeletedAndHasReReply(replyId));
//    }
//
//}