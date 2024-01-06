package com.zerobase.stockservice.service;

import com.zerobase.stockservice.domain.Company;
import com.zerobase.stockservice.domain.Dividend;
import com.zerobase.stockservice.dto.ScrapedResult;
import com.zerobase.stockservice.repository.CompanyRepository;
import com.zerobase.stockservice.repository.DividendRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FinanceServiceTest {
    @InjectMocks private FinanceService financeService;
    @Mock private CompanyRepository companyRepository;
    @Mock private DividendRepository dividendRepository;

    @DisplayName("기업명으로 배당금 정보 조회")
    @Test
    void getDividendByCompanyName() {
        //given
        String ticker = "NVDA";
        String companyName = "NVIDIA";
        given(companyRepository.findByName(anyString()))
                .willReturn(Optional.of(Company.builder().id(1L).name(companyName).ticker(ticker).build()));
        LocalDateTime now = LocalDateTime.now();
        String dividend = "0.05";
        given(dividendRepository.findAllByCompanyId(anyLong()))
                .willReturn(List.of(Dividend.builder().id(1L).dividend(dividend).date(now).companyId(1L).build()));
        //when
        ScrapedResult scrapedResult = financeService.getDividendByCompanyName(companyName);
        //then
        assertThat(scrapedResult.getCompany().getName()).isEqualTo(companyName);
        assertThat(scrapedResult.getCompany().getTicker()).isEqualTo(ticker);
        assertThat(scrapedResult.getDividends().size()).isEqualTo(1);
        assertThat(scrapedResult.getDividends().get(0).getDividend()).isEqualTo(dividend);
        assertThat(scrapedResult.getDividends().get(0).getDate()).isEqualTo(now);
    }
}