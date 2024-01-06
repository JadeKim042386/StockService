package com.zerobase.stockservice.service;

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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {
    @InjectMocks private CompanyService companyService;
    @Mock private Scraper scraper;
    @Mock private CompanyRepository companyRepository;
    @Mock private DividendRepository dividendRepository;
    @Captor private ArgumentCaptor<List<Dividend>> dividendCaptor;

    @DisplayName("ticker로 DB에 데이터가 존재하는지 확인 후 기업 정보와 배당급 정보를 스크래핑하여 저장")
    @Test
    void storeCompanyAndDividend() {
        //given
        String name = "NVIDIA";
        String ticker = "NVDA";
        CompanyDto companyDto = CompanyDto.builder()
                .name(name)
                .ticker(ticker)
                .build();
        given(companyRepository.existsByTicker(anyString())).willReturn(false);
        given(scraper.scrapCompanyByTicker(anyString()))
                .willReturn(Optional.of(companyDto));
        given(companyRepository.save(any()))
                .willReturn(Company.builder()
                            .id(1L)
                            .name(name)
                            .ticker(ticker)
                            .build());
        String divide = "0.04";
        LocalDateTime time = LocalDateTime.now();
        DividendDto dividendDto = DividendDto.builder()
                .dividend(divide)
                .date(time)
                .build();
        given(scraper.scrap(any()))
                .willReturn(ScrapedResult.of(companyDto, List.of(dividendDto)));
        Dividend dividend = Dividend.builder()
                .companyId(1L)
                .date(time)
                .dividend(divide)
                .build();
        given(dividendRepository.saveAll(anyList()))
                .willReturn(List.of(dividend));
        //when
        CompanyDto dto = companyService.save(ticker);
        ArgumentCaptor<Company> companyCaptor = ArgumentCaptor.forClass(Company.class);
        //then
        verify(companyRepository, times(1)).save(companyCaptor.capture());
        verify(dividendRepository, times(1)).saveAll(dividendCaptor.capture());
        assertThat(companyCaptor.getValue().getName()).isEqualTo(name);
        assertThat(companyCaptor.getValue().getTicker()).isEqualTo(ticker);
        assertThat(dividendCaptor.getValue().get(0).getDividend()).isEqualTo(divide);
        assertThat(dividendCaptor.getValue().get(0).getDate()).isEqualTo(time);
        assertThat(dto.getName()).isEqualTo(name);
        assertThat(dto.getTicker()).isEqualTo(ticker);
    }
}