package com.zerobase.stockservice.scheduler;

import com.zerobase.stockservice.domain.Company;
import com.zerobase.stockservice.dto.CompanyDto;
import com.zerobase.stockservice.dto.ScrapedResult;
import com.zerobase.stockservice.dto.constants.CacheKey;
import com.zerobase.stockservice.repository.CompanyRepository;
import com.zerobase.stockservice.repository.DividendRepository;
import com.zerobase.stockservice.scraper.Scraper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
@Slf4j
@Component
@EnableCaching
@RequiredArgsConstructor
public class ScraperScheduler {
    private final Scraper yahooFinanceScraper;
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    /**
     * 매일 자정에 DB 저장된 전체 기업의 배당금을 스크래핑하고 캐시를 비운다.
     */
    @CacheEvict(value = CacheKey.KEY_FINANCE, allEntries = true)
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
                            .map(e -> e.toEntity(company))
                            .toList()
            );
            try {
                Thread.sleep(3000); //3 seconds
            } catch (InterruptedException e) {
                log.error("스케줄러로 배당금 정보를 스크래핑하던 중 스레드 인터럽트가 발생했습니다. - {}", company.getName());
                Thread.currentThread().interrupt();
            }
        }
    }
}
