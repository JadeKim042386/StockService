package com.zerobase.stockservice.repository;

import com.zerobase.stockservice.domain.Dividend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface DividendRepository extends JpaRepository<Dividend, Long> {
    List<Dividend> findAllByCompanyId(Long companyId);
    boolean existsByCompanyIdAndDate(Long companyId, LocalDateTime date);
}
