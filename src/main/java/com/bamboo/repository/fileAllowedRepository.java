package com.bamboo.repository;


import com.bamboo.entity.FileConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface fileAllowedRepository extends JpaRepository<FileConfig,Long> {
}
