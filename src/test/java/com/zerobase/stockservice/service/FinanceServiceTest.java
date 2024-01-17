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
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FinanceServiceTest {
    @InjectMocks private FinanceService financeService;
    @Mock private CompanyRepository companyRepository;

    @DisplayName("기업명으로 배당금 정보 조회")
    @Test
    void getDividendByCompanyName() {
        //given
        String ticker = "NVDA";
        String companyName = "NVIDIA";
        LocalDateTime now = LocalDateTime.now();
        String dividend = "0.05";
        Set<Dividend> dividends = Set.of(Dividend.builder().id(1L).amount(dividend).date(now).build());
        given(companyRepository.findByNameIgnoreCase(anyString()))
                .willReturn(Optional.of(Company.builder()
                        .id(1L)
                        .name(companyName)
                        .ticker(ticker)
                        .dividends(dividends)
                        .build()));
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