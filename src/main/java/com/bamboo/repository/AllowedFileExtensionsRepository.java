package com.bamboo.repository;

import com.bamboo.entity.AllowedFileExtensions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface AllowedFileExtensionsRepository extends JpaRepository<AllowedFileExtensions, Long> {
    @Modifying
    @Query(value = "UPDATE allowed_file_extension SET file_config_id = 1",nativeQuery = true)
    void updateConfigId();

    @Modifying
    @Query(value = "DELETE FROM allowed_file_extension WHERE file_config_id = 1",nativeQuery = true)
    void deleteExtensions();

    @Query(value = "SELECT GROUP_CONCAT(extension SEPARATOR ',') FROM allowed_file_extension WHERE file_config_id = :fileConfigId", nativeQuery = true)
    String findExtensionsByFileConfigId(@Param("fileConfigId") long fileConfigId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO allowed_file_extension(file_config_id,extension) values(1,'mp4'),(1,'avi'),(1,'mkv'),(1,'mpg')" +
            ",(1,'doc'),(1,'docx'),(1,'xls'),(1,'xlsx'),(1,'ppt'),(1,'pptx'),(1,'pdf'),(1,'txt'),(1,'csv'),(1,'json'),(1,'rtf'),(1,'odt')" +
            ",(1,'zip'),(1,'tar.gz'),(1,'rar')" +
            ",(1,'jpeg'),(1,'jpg'),(1,'png'),(1,'gif')",nativeQuery = true)
    void defualtExtensions();
}