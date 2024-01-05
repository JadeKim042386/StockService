package com.zerobase.stockservice.repository;

import com.zerobase.stockservice.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
