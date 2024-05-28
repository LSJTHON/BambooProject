package com.bamboo.entity;

import com.bamboo.dto.BoardFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "board")
@Getter
@Setter
@ToString
public class Board extends BaseTimeEntity{

    @Id
    @Column(name = "board_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_email")
    private Member member;

    @Column(nullable = false, name = "board_title")
    private String title;

    @Lob
    @Column(name = "board_content", nullable = false, columnDefinition = "varchar(10000)")
    private String content;

    @Column(name = "board_is_deleted")
    private boolean isDeleted;

    @Column(name = "board_is_restored")
    private boolean isRestored;

    private Long good;

    private Long hit;

    public void updateBoard(BoardFormDto boardFormDto) {
        this.title = boardFormDto.getTitle();
        this.content = boardFormDto.getContent();
    }
}
