package com.zerobase.stockservice.scheduler;

import com.zerobase.stockservice.domain.Company;
import com.zerobase.stockservice.domain.Dividend;
import com.zerobase.stockservice.dto.CompanyDto;
import com.zerobase.stockservice.dto.DividendDto;
import com.zerobase.stockservice.dto.ScrapedResult;
import com.zerobase.stockservice.repository.CompanyRepository;
import com.zerobase.stockservice.repository.DividendRepository;
import com.zerobase.stockservice.scraper.Scraper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ScraperSchedulerTest {
    @InjectMocks private ScraperScheduler scraperScheduler;
    @Mock private Scraper scraper;
    @Mock private CompanyRepository companyRepository;
    @Mock private DividendRepository dividendRepository;
    @Captor private ArgumentCaptor<List<Dividend>> dividendCaptor;

    @DisplayName("특정 시간마다 DB에 저장된 기업의 배당금 스크래핑")
    @Test
    void yahooFinanceSchedule() {
        //given
        String name = "NVIDIA";
        String ticker = "NVDA";
        Company company = Company.builder()
                .id(1L)
                .name(name)
                .ticker(ticker)
                .build();
        LocalDateTime now = LocalDateTime.now();
        String divide = "0.04";
        DividendDto dividendDto = DividendDto.of(now, divide);
        given(companyRepository.findAll()).willReturn(List.of(company));
        given(scraper.scrap(any())).willReturn(ScrapedResult.of(CompanyDto.fromEntity(company), List.of(dividendDto)));
        given(dividendRepository.existsByCompanyIdAndDate(anyLong(), any())).willReturn(false);
        given(dividendRepository.saveAll(anyList())).willReturn(List.of(dividendDto.toEntity(company)));
        //when
        scraperScheduler.yahooFinanceSchedule();
        //then
        verify(dividendRepository, times(1)).saveAll(dividendCaptor.capture());
        assertThat(dividendCaptor.getValue().get(0).getDividend()).isEqualTo(divide);
        assertThat(dividendCaptor.getValue().get(0).getDate()).isEqualTo(now);
    }
}