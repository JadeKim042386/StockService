package com.zerobase.stockservice.service;

import com.zerobase.stockservice.domain.Company;
import com.zerobase.stockservice.dto.CompanyDto;
import com.zerobase.stockservice.dto.DividendDto;
import com.zerobase.stockservice.dto.ScrapedResult;
import com.zerobase.stockservice.dto.constants.CacheKey;
import com.zerobase.stockservice.exception.CompanyException;
import com.zerobase.stockservice.exception.ErrorCode;
import com.zerobase.stockservice.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FinanceService {
    private final CompanyRepository companyRepository;

    @Cacheable(value = CacheKey.KEY_FINANCE, key = "#companyName")
    public ScrapedResult getDividendByCompanyName(String companyName) {
        Company company = companyRepository.findByNameIgnoreCase(companyName)
            .orElseThrow(() -> new CompanyException(ErrorCode.NOT_FOUND_COMPANY));
        return ScrapedResult.of(
                CompanyDto.fromEntity(company),
                company.getDividends().stream().map(DividendDto::fromEntity).collect(Collectors.toList())
        );
    }
}
