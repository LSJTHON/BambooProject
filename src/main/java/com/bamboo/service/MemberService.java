package com.bamboo.service;

import com.bamboo.constant.Role;
import com.bamboo.dto.MemberFormDto;
import com.bamboo.entity.Board;
import com.bamboo.entity.Member;
import com.bamboo.repository.BoardRepository;
import com.bamboo.repository.MemberRepository;
import com.bamboo.repository.ReReplyRepository;
import com.bamboo.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;
    private final ReReplyRepository reReplyRepository;


    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Member save(MemberFormDto dto){
        return memberRepository.save(Member.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                //패스워드 암호화
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .role(Role.USER)
                .build());
    }

    public boolean findByEmail(String email){
        return memberRepository.existsByEmail(email);
    }

    public Member kakaoSave(String email, String nickname,String role){

        if(role.equals("ADMIN")){
            Member user = memberRepository.findByEmail(email)
                    .orElse(Member.builder()
                            .email(email)
                            .name(nickname)
                            .role(Role.ADMIN)
                            .build());
            return memberRepository.save(user);
        }else{
            Member user = memberRepository.findByEmail(email)
                    .orElse(Member.builder()
                            .email(email)
                            .name(nickname)
                            .role(Role.USER)
                            .build());
            return memberRepository.save(user);
        }

    }

    public Member updatedDelete(String email){

        System.out.println("정지할 이메일이 어떻게 날아오노?" + email);

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("정지할 이메일을 찾을 수 없습니다."));

        memberRepository.deletedEmail(email);
        replyRepository.deletedReply(email);
        reReplyRepository.deletedRe_Reply(email);
        boardRepository.deletedBoard(email);
        return member;
    }

    public Member updatedRestore(String email){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("복구할 이메일을 찾을 수 없습니다."));
        memberRepository.restoredEmail(email);
        replyRepository.restoredReply(email);
        reReplyRepository.restoredRe_Reply(email);
        boardRepository.restoredBoard(email);
        return member;
    }


    @Transactional
    public Member modifyMember(String name, String password, String email){

        System.out.println("이게 이메일 정보다 이말이야 :  "+email);

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("수정할 계정 정보를 찾을 수 없습니다."));

        //사용자의 이름과 비밀번호 수정
        member.updateMemberInfo(name,bCryptPasswordEncoder.encode(password));

        return member;
    }


//    @Transactional(readOnly = true)
//    public Page<Member> getMemberPage(String searchBy, String searchQuery, Pageable pageable) {
//        if ("email".equals(searchBy)) {
//            return memberRepository.findByEmailContaining(searchQuery, pageable);
//        } else if ("name".equals(searchBy)) {
//            return memberRepository.findByNameContaining(searchQuery, pageable);
//        } else {
//            return memberRepository.findAll(pageable);
//        }
//    }
    @Transactional(readOnly = true)
    public Page<Member> getDeletedMembersPage(String searchBy, String searchQuery, Pageable pageable) {
        if ("email".equals(searchBy)) {
            return memberRepository.findByIsDeletedAndEmailContaining(true, searchQuery, pageable);
        } else if ("name".equals(searchBy)) {
            return memberRepository.findByIsDeletedAndNameContaining(true, searchQuery, pageable);
        } else {
            return memberRepository.findByIsDeleted(true, pageable);
        }
    }
    //사용자의 정지 여부 판단 MemberRepository 호출
    public boolean isUserDeleted(String email) {
        Optional<Integer> isDeleted = memberRepository.findIsDeletedByEmail(email);
        return isDeleted.orElse(0) == 1;
    }

    public Member saveAdmin(){
        return memberRepository.save(Member.builder()
                .email("dltjdhwd1235@naver.com")
                .name("관리자계정")
                //패스워드 암호화
                .password(bCryptPasswordEncoder.encode("12341234"))
                .role(Role.ADMIN)
                .build());
    }

}
