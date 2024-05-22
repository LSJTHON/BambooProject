package com.bamboo.service;

import com.bamboo.constant.Role;
import com.bamboo.dto.MemberFormDto;
import com.bamboo.entity.Member;
import com.bamboo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
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

    public Member findByEmail(String email){
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("유저의 이메일을 찾을 수 없습니다."));
    }


    public Member kakaoSave(String email, String nickname){
        Member user = memberRepository.findByEmail(email)
                .orElse(Member.builder()
                        .email(email)
                        .name(nickname)
                        .role(Role.USER)
                        .build());
        return memberRepository.save(user);
    }

    public Member updatedDelete(String email){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("정지할 이메일을 찾을 수 없습니다."));

        memberRepository.deletedEmail(email);
        return member;
    }


    @Transactional
    public Member modifyMember(String name, String password, String email){

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("수정할 계정 정보를 찾을 수 없습니다."));


        //사용자의 이름과 비밀번호 수정
        member.updateMemberInfo(name,bCryptPasswordEncoder.encode(password));

        return member;
    }

}
