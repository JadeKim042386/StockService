package com.zerobase.stockservice.repository;

import com.zerobase.stockservice.domain.Dividend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface DividendRepository extends JpaRepository<Dividend, Long> {
    boolean existsByCompanyIdAndDate(Long companyId, LocalDateTime date);

    void deleteAllByCompanyId(Long id);
}
