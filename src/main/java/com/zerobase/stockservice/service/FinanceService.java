package com.zerobase.stockservice.service;

import com.zerobase.stockservice.domain.Company;
import com.zerobase.stockservice.domain.Dividend;
import com.zerobase.stockservice.dto.CompanyDto;
import com.zerobase.stockservice.dto.DividendDto;
import com.zerobase.stockservice.dto.ScrapedResult;
import com.zerobase.stockservice.repository.CompanyRepository;
import com.zerobase.stockservice.repository.DividendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FinanceService {
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    public ScrapedResult getDividendByCompanyName(String companyName) {
        //TODO: 예외 처리
        Company company = companyRepository.findByName(companyName)
            .orElseThrow(() -> new EntityNotFoundException());
        List<Dividend> dividends = dividendRepository.findAllByCompanyId(company.getId());

        return ScrapedResult.of(
                CompanyDto.fromEntity(company),
                dividends.stream().map(DividendDto::fromEntity).collect(Collectors.toList())
        );
    }
}
