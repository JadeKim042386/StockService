package com.zerobase.stockservice.repository;

import com.zerobase.stockservice.domain.Company;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    boolean existsByTicker(String ticker);

    Optional<Company> findByName(String name);

    List<Company> findByNameStartingWithIgnoreCase(Pageable pageable, String s);

    Optional<Company> findByTicker(String ticker);
}
