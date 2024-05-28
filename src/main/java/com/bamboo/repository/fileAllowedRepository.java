package com.bamboo.repository;


import com.bamboo.entity.FileConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface fileAllowedRepository extends JpaRepository<FileConfig,Long> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO file_config(max_file_count,max_file_size) values(10,20)",nativeQuery = true)
    void defualtFileAllowed();

}
