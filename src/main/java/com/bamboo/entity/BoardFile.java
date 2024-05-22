package com.bamboo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "board_file")
@Getter
@Setter
public class BoardFile {

    @Id
    @Column(name = "board_file_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String fileName;

    private String oriFileName;

    private String fileUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    public void updateBoardFile(String oriFileName, String fileName, String fileUrl) {
        this.oriFileName = oriFileName;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
    }
}
