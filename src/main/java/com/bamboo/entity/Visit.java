package com.bamboo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "visit")
@Getter
@Setter
@ToString
public class Visit {

    @Id
    @Column(name = "visit_date")
    private LocalDate date; // xxxx-xx-xx

    private Long visitNum;
}
