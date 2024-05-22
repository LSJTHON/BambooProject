package com.bamboo.repository;

import com.bamboo.constant.Role;
import com.bamboo.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("멤버 생성 테스트")
    public void createMemberTest() {
        Member member = new Member();
        member.setEmail("test@test.com");
        member.setName("홍길동");
        String password = passwordEncoder.encode("1234");
        member.setPassword(password);
        member.setRole(Role.ADMIN);
        memberRepository.save(member);
    }
}