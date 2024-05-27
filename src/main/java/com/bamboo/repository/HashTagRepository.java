package com.bamboo.repository;

import com.bamboo.entity.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HashTagRepository extends JpaRepository<Hashtag, Long> {

    Optional<Hashtag> findByName(String name);
}
