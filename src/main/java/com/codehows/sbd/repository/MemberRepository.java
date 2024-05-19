package com.codehows.sbd.repository;

import com.codehows.sbd.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByEmail(String email); //사용자 정보를 이메일로 가져와
}
