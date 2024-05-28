package com.bamboo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "file_config")
@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(value = {AuditingEntityListener.class})
public class FileConfig {

    @Id
    @Column(name = "file_config_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long maxFileCount;

    private Long maxFileSize;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime regTime;

    public void update(Long maxFileCount, Long maxFileSize){
        this.maxFileCount = maxFileCount;
        this.maxFileSize = maxFileSize;
    }

    @Builder
    public FileConfig(Long maxFileSize, Long maxFileCount){
        this.maxFileSize = maxFileSize;
        this.maxFileCount = maxFileCount;
    }



}
