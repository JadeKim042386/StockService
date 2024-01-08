package com.zerobase.stockservice.service;

import com.zerobase.stockservice.domain.Company;
import com.zerobase.stockservice.dto.CompanyDto;
import com.zerobase.stockservice.dto.ScrapedResult;
import com.zerobase.stockservice.exception.CompanyException;
import com.zerobase.stockservice.exception.ErrorCode;
import com.zerobase.stockservice.repository.CompanyRepository;
import com.zerobase.stockservice.repository.DividendRepository;
import com.zerobase.stockservice.scraper.Scraper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyService {

    private final Scraper yahooFinanceScraper;
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    @Transactional
    public CompanyDto save(String ticker) {
        if (companyRepository.existsByTicker(ticker)) {
            throw new CompanyException(ErrorCode.ALREADY_EXIST_TICKER);
        }
        return storeCompanyAndDividend(ticker);
    }

    public Page<CompanyDto> findAllCompany(Pageable pageable) {
        return companyRepository.findAll(pageable)
                .map(CompanyDto::fromEntity);
    }

    public List<String> getCompanyNamesByKeyword(String keyword) {
        return companyRepository.findByNameStartingWithIgnoreCase(Pageable.ofSize(10), keyword).stream()
                .map(Company::getName).collect(Collectors.toList());
    }

    @Transactional
    public String deleteCompany(String ticker) {
        Company company = companyRepository.findByTicker(ticker)
                .orElseThrow(() -> new CompanyException(ErrorCode.NOT_FOUND_COMPANY));
        dividendRepository.deleteAllByCompanyId(company.getId());
        companyRepository.delete(company);
        return company.getName();
    }

    private CompanyDto storeCompanyAndDividend(String ticker) {
        CompanyDto companyDto = yahooFinanceScraper.scrapCompanyByTicker(ticker)
                .orElseThrow(() -> new CompanyException(ErrorCode.FAILED_SCRAP));
        Company companyEntity = companyRepository.save(companyDto.toEntity());
        ScrapedResult scrapedResult = yahooFinanceScraper.scrap(companyDto);
        dividendRepository.saveAll(
                scrapedResult.getDividends().stream()
                        .map(e -> e.toEntity(companyEntity.getId()))
                        .collect(Collectors.toList())
        );

        return companyDto;
    }
}
