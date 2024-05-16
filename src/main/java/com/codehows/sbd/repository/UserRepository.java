package com.codehows.sbd.repository;

import com.codehows.sbd.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email); //사용자 정보를 이메일로 가져와
}
