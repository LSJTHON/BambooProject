package com.bamboo.repository;

import com.bamboo.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,String> {
    Optional<Member> findByEmail(String email); //사용자 정보를 이메일로 가져와

    //아이디 중복확인 용도
    boolean existsByEmail(String email);

    //사용자 영구 정지
    @Modifying
    @Transactional
    @Query(value = "UPDATE member SET member_is_deleted = 1 WHERE member_email = :email", nativeQuery = true)
    void deletedEmail(@Param("email") String email);


    //사용자의 정지상태 복구
    @Modifying
    @Transactional
    @Query(value = "UPDATE member SET member_is_deleted = 0 WHERE member_email = :email", nativeQuery = true)
    void restoredEmail(@Param("email") String email);


    //사용자의 정지여부 판단 쿼리
//    @Query(value = "SELECT member_is_deleted FROM member WHERE member_email = :email", nativeQuery = true)
//    Optional<Integer> findIsDeletedByEmail(@Param("email") String email);
    @Query(value = "SELECT CAST(member_is_deleted AS UNSIGNED) FROM member WHERE member_email = :email", nativeQuery = true)
    Optional<Integer> findIsDeletedByEmail(@Param("email") String email);



    Page<Member> findByIsDeleted(boolean isDeleted, Pageable pageable);

    Page<Member> findByIsDeletedAndEmailContaining(boolean isDeleted, String email, Pageable pageable);

    Page<Member> findByIsDeletedAndNameContaining(boolean isDeleted, String name, Pageable pageable);


    List<Member> findByRegTimeBetween(LocalDateTime startDate, LocalDateTime endDate);
}
