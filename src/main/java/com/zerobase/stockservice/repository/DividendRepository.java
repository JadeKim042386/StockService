package com.zerobase.stockservice.repository;

import com.zerobase.stockservice.domain.Dividend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DividendRepository extends JpaRepository<Dividend, Long> {
}
