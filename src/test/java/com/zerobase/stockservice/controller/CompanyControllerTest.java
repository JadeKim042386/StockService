package com.zerobase.stockservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.stockservice.dto.CompanyDto;
import com.zerobase.stockservice.service.CompanyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CompanyController.class)
class CompanyControllerTest {
    @Autowired private MockMvc mvc;
    @MockBean private CompanyService companyService;
    @Autowired private ObjectMapper objectMapper;

    @DisplayName("[POST] 특정 기업의 ticker를 통한 배당금 정보 저장 요청")
    @WithMockUser
    @Test
    void addCompany() throws Exception {
        //given
        String ticker = "NVDA";
        String name = "NVIDIA";
        given(companyService.save(anyString()))
                .willReturn(CompanyDto.of(ticker, name));
        //when
        mvc.perform(
                post("/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                CompanyDto.of(ticker, null)
                        ))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticker").value(ticker))
                .andExpect(jsonPath("$.name").value(name));
        //then
    }

    @DisplayName("[GET] 기업 정보 전체 조회")
    @Test
    void searchCompany() throws Exception {
        //given
        String ticker = "NVDA";
        String name = "NVIDIA";
        CompanyDto companyDto = CompanyDto.of(ticker, name);
        Pageable pageable = Pageable.ofSize(2);
        Page<CompanyDto> page = new PageImpl<>(List.of(companyDto, companyDto), pageable, 3);
        given(companyService.findAllCompany(any()))
                .willReturn(page);
        //when
        mvc.perform(
                get("/company")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.content.size()").value(2))
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.content[0].ticker").value(ticker))
                .andExpect(jsonPath("$.content[0].name").value(name));
        //then
    }

    @DisplayName("[GET] 입력 키워드로 시작하는 모든 기업 조회 요청")
    @Test
    void autocomplete() throws Exception {
        //given
        String name = "NVIDIA";
        given(companyService.getCompanyNamesByKeyword(anyString()))
                .willReturn(List.of(name));
        //when
        mvc.perform(
                get("/company/autocomplete")
                        .queryParam("keyword", "n")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0]").value(name));
        //then
    }
}