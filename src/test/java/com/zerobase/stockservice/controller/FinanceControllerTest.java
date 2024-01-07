package com.zerobase.stockservice.controller;

import com.zerobase.stockservice.dto.CompanyDto;
import com.zerobase.stockservice.dto.DividendDto;
import com.zerobase.stockservice.dto.ScrapedResult;
import com.zerobase.stockservice.service.FinanceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FinanceController.class)
class FinanceControllerTest {
    @Autowired private MockMvc mvc;
    @MockBean private FinanceService financeService;

    @DisplayName("기업명으로 배당금 정보 조회 요청")
    @Test
    void searchFinance() throws Exception {
        //given
        String ticker = "NVDA";
        String companyName = "NVIDIA";
        LocalDateTime now = LocalDateTime.now();
        String dividend = "0.05";
        CompanyDto companyDto = CompanyDto.of(ticker, companyName);
        List<DividendDto> dividendDtos = List.of(DividendDto.of(now, dividend));
        given(financeService.getDividendByCompanyName(anyString()))
                .willReturn(ScrapedResult.of(companyDto, dividendDtos));
        //when
        mvc.perform(
                get("/finance/dividend/" + companyName)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.company.ticker").value(ticker))
                .andExpect(jsonPath("$.company.name").value(companyName))
                .andExpect(jsonPath("$.dividends.size()").value(1))
                .andExpect(jsonPath("$.dividends[0].date").value(now.toString()))
                .andExpect(jsonPath("$.dividends[0].dividend").value(dividend));
        //then
    }
}