package com.zerobase.stockservice.dto;

import com.zerobase.stockservice.domain.Company;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ScrapedResult {
    private CompanyDto company;
    private List<DividendDto> dividends;

    public ScrapedResult() {
        this.dividends = new ArrayList<>();
    }
}
