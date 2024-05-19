package com.bamboo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "file_config")
@Getter
@Setter
@ToString
@EntityListeners(value = {AuditingEntityListener.class})
public class FileConfig {

    @Id
    @Column(name = "file_config_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long maxFileCount;

    private Long maxFileSize;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime regTime;

}
