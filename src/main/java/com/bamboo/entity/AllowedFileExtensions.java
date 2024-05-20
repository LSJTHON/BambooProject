package com.bamboo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "allowed_file_extension")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class AllowedFileExtensions {

    @Id
    @Column(name = "allowed_file_extension_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_config_id")
    private FileConfig fileConfig;

    private String extension;

    @Builder
    public AllowedFileExtensions(String extension) {
        this.extension = extension;
    }

}
