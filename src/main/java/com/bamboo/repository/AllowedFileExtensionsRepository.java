package com.bamboo.repository;

import com.bamboo.entity.AllowedFileExtensions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AllowedFileExtensionsRepository extends JpaRepository<AllowedFileExtensions, Long> {
    @Modifying
    @Query(value = "UPDATE allowed_file_extension SET file_config_id = 1",nativeQuery = true)
    void updateConfigId();

    @Modifying
    @Query(value = "DELETE FROM allowed_file_extension WHERE file_config_id = 1",nativeQuery = true)
    void deleteExtensions();

    @Query(value = "SELECT GROUP_CONCAT(extension SEPARATOR ',') FROM allowed_file_extension WHERE file_config_id = :fileConfigId", nativeQuery = true)
    String findExtensionsByFileConfigId(@Param("fileConfigId") long fileConfigId);

}