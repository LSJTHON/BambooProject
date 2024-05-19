package com.bamboo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "allowed_file_extension")
@Getter
@Setter
@ToString
public class AllowedFileExtensions {

    @Id
    @Column(name = "allowed_file_extension_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_config_id")
    private FileConfig fileConfig;

    private String extension;
}
