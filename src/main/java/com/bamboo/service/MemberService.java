package com.bamboo.service;



import com.bamboo.constant.Role;
import com.bamboo.dto.AddMemberRequest;
import com.bamboo.entity.Member;
import com.bamboo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long save(AddMemberRequest dto){
        return memberRepository.save(Member.builder()
                .email(dto.getEmail())
                //패스워드 암호화
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .role(Role.USER)
                .build()).getId();
    }

    public Member findByEmail(String email){
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }


    public Member kakaoSave(String email, String nickname){
        Member user = memberRepository.findByEmail(email)
                .orElse(Member.builder()
                        .email(email)
                        .nickname(nickname)
                        .role(Role.USER)
                        .build());
        return memberRepository.save(user);
    }


}





