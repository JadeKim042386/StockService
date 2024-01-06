package com.zerobase.stockservice.scheduler;

import com.zerobase.stockservice.domain.Company;
import com.zerobase.stockservice.dto.CompanyDto;
import com.zerobase.stockservice.dto.ScrapedResult;
import com.zerobase.stockservice.repository.CompanyRepository;
import com.zerobase.stockservice.repository.DividendRepository;
import com.zerobase.stockservice.scraper.Scraper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
@Slf4j
@Component
@RequiredArgsConstructor
public class ScraperScheduler {
    private final Scraper yahooFinanceScraper;
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    // 매일 자정에 실행
    @Scheduled(cron = "${scheduler.scrap.yahoo}")
    public void yahooFinanceSchedule() {
        log.info("=> scraping scheduler is started");
        List<Company> companies = companyRepository.findAll();
        for (Company company : companies) {
            log.info("{} scraping scheduler is started", company.getName());
            ScrapedResult scrapedResult = yahooFinanceScraper.scrap(CompanyDto.fromEntity(company));
            dividendRepository.saveAll(
                    scrapedResult.getDividends().stream()
                            .filter(e -> !dividendRepository.existsByCompanyIdAndDate(company.getId(), e.getDate()))
                            .map(e -> e.toEntity(company.getId()))
                            .toList()
            );
        }
        try {
            Thread.sleep(3000); //3 seconds
        } catch (InterruptedException e) {
            //TODO: 예외 처리
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
}
