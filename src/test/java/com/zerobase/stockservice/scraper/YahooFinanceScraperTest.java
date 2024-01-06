package com.zerobase.stockservice.scraper;

import com.zerobase.stockservice.dto.CompanyDto;
import com.zerobase.stockservice.dto.ScrapedResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class YahooFinanceScraperTest {
    @InjectMocks private YahooFinanceScraper yahooFinanceScraper;

    @DisplayName("특정 기업의 배당금 정보 스크래핑")
    @Test
    void scrap() {
        //given
        String ticker = "NVDA";
        String name = "NVIDIA";
        CompanyDto companyDto = CompanyDto.builder()
                                    .ticker(ticker)
                                    .name(name)
                                    .build();
        //when
        ScrapedResult scrapedResult = yahooFinanceScraper.scrap(companyDto);
        //then
        assertThat(scrapedResult.getCompany().getTicker()).isEqualTo(ticker);
        assertThat(scrapedResult.getCompany().getName()).isEqualTo(name);
        assertThat(scrapedResult.getDividends()).isNotEmpty();
    }

    @DisplayName("ticker를 통해 기업 정보 스크래핑")
    @Test
    void scrapCompanyByTicker() {
        //given
        String ticker = "NVDA";
        //when
        Optional<CompanyDto> companyDto = yahooFinanceScraper.scrapCompanyByTicker(ticker);
        //then
        assertThat(companyDto.isPresent()).isEqualTo(true);
        assertThat(companyDto.get().getTicker()).isEqualTo(ticker);
        assertThat(companyDto.get().getName()).isEqualTo("NVIDIA Corporation");
    }

}