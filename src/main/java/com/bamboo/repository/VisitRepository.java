package com.bamboo.repository;

import com.bamboo.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface VisitRepository extends JpaRepository<Visit, LocalDate> {
    List<Visit> findByDateBetween(LocalDate startDate, LocalDate endDate);
}
