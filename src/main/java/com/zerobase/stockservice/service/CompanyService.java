package com.zerobase.stockservice.service;

import com.zerobase.stockservice.domain.Company;
import com.zerobase.stockservice.dto.CompanyDto;
import com.zerobase.stockservice.dto.ScrapedResult;
import com.zerobase.stockservice.repository.CompanyRepository;
import com.zerobase.stockservice.repository.DividendRepository;
import com.zerobase.stockservice.scraper.Scraper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
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
            //TODO: 예외 처리
            throw new RuntimeException("already exists ticker - " + ticker);
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
        //TODO: 예외 처리
        Company company = companyRepository.findByTicker(ticker)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 기업입니다."));
        dividendRepository.deleteAllByCompanyId(company.getId());
        companyRepository.delete(company);
        return company.getName();
    }

    private CompanyDto storeCompanyAndDividend(String ticker) {
        //TODO: 예외 처리
        CompanyDto companyDto = yahooFinanceScraper.scrapCompanyByTicker(ticker)
                .orElseThrow(() -> new RuntimeException("failed to scrap ticker - " + ticker));
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
