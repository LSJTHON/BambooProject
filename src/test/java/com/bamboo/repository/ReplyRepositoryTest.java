package com.bamboo.repository;

import com.bamboo.constant.Role;
import com.bamboo.entity.Board;
import com.bamboo.entity.Member;
import com.bamboo.entity.ReReply;
import com.bamboo.entity.Reply;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class ReplyRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    ReplyRepository replyRepository;
    @Autowired
    ReReplyRepository reReplyRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PersistenceContext
    EntityManager em;

    public Member createMember() {
        Member member = new Member();
        member.setEmail("test@test.com");
        member.setName("홍길동");
        String password = passwordEncoder.encode("1234");
        member.setPassword(password);
        member.setRole(Role.ADMIN);
        return memberRepository.save(member);
    }

    public Board createBoard() {
        Member member = createMember();

        Board board = new Board();
        board.setMember(member);
        board.setTitle("테스트 제목");
        board.setContent("테스트 내용");
        board.setDeleted(false);
        board.setRestored(false);
        board.setGood(0L);
        board.setHit(0L);

        return boardRepository.save(board);
    }

    @Test
    @DisplayName("댓글 생성 테스트")
    public void createReplyTest() {
        Member member = createMember();
        Board board = createBoard();

        Reply reply = new Reply();
        reply.setContent("테스트 댓글");
        reply.setMember(member);
        reply.setBoard(board);
        reply.setDeleted(false);

        replyRepository.save(reply);

        em.flush();
        em.clear();

        Reply savedReply = replyRepository.findById(1L)
                .orElseThrow(EntityNotFoundException::new);

        assertEquals(member.getEmail(), savedReply.getMember().getEmail());
    }

    @Test
    @DisplayName("조건에 맞는 댓글 목록 조회 테스트")
    public void findActiveAndReferencedRepliesTest() {
        Member member = createMember();
        Board board = createBoard();

        for (int i = 0; i < 4; i++) {
            Reply reply = new Reply();
            reply.setContent("테스트 댓글" + i);
            reply.setMember(member);
            reply.setBoard(board);
            if (i<2){
                reply.setDeleted(true);
            }else{
                reply.setDeleted(false);
            }
            replyRepository.save(reply);

            ReReply reReply = new ReReply();
            reReply.setContent("테스트 대댓글" + i);
            reReply.setMember(member);
            reReply.setBoard(board);
            reReply.setReply(reply);
            if (i%2==0){
                reReply.setDeleted(false);
            }else {
                reReply.setDeleted(true);
            }
            reReplyRepository.save(reReply);

            System.out.println("생성 댓글:"+reply.getContent()+", 생성 댓글 상태:"+reply.isDeleted()+", 생성 대댓글:"+reReply.getContent()+", 생성 대댓글 상태:"+reReply.isDeleted());
        }

        List<Reply> replies = replyRepository.findActiveAndReferencedReplies();

        for (Reply reply : replies) {
            System.out.println("댓글명: "+reply.getContent()+", 댓글 상태:"+reply.isDeleted());
        }
    }


    @Test
    @DisplayName("조건에 맞는 대댓글 목록 조회 테스트")
    public void findAllNotDeletedTest(){
        Member member = createMember();
        Board board = createBoard();

        for (int i = 0; i < 4; i++) {
            Reply reply = new Reply();
            reply.setContent("테스트 댓글" + i);
            reply.setMember(member);
            reply.setBoard(board);
            if (i<2){
                reply.setDeleted(true);
            }else{
                reply.setDeleted(false);
            }
            replyRepository.save(reply);

            ReReply reReply = new ReReply();
            reReply.setContent("테스트 대댓글" + i);
            reReply.setMember(member);
            reReply.setBoard(board);
            reReply.setReply(reply);
            if (i%2==0){
                reReply.setDeleted(false);
            }else {
                reReply.setDeleted(true);
            }
            reReplyRepository.save(reReply);

            System.out.println("생성 댓글:"+reply.getContent()+", 생성 댓글 상태:"+reply.isDeleted()+", 생성 대댓글:"+reReply.getContent()+", 생성 대댓글 상태:"+reReply.isDeleted());
        }

        List<ReReply> reReplies = reReplyRepository.findAllNotDeleted();

        for (ReReply reReply : reReplies){
            System.out.println("대댓글 내용:"+reReply.getContent()+", 대댓글 작성자:"+reReply.getMember().getName());
        }
    }
}