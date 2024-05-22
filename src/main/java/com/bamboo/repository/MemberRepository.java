package com.bamboo.repository;

import com.bamboo.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,String> {
    Optional<Member> findByEmail(String email); //사용자 정보를 이메일로 가져와

    @Modifying
    @Transactional
    @Query(value = "UPDATE member SET member_is_deleted = 1 WHERE member_email = :email", nativeQuery = true)
    void deletedEmail(@Param("email") String email);
}
