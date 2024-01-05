package com.zerobase.stockservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ScrapedResult {
    private CompanyDto company;
    private List<DividendDto> dividends;

    public static ScrapedResult of (CompanyDto company, List<DividendDto> dividends) {
        return new ScrapedResult(company, dividends);
    }
}
