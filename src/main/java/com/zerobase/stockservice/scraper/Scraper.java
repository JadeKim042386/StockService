package com.zerobase.stockservice.scraper;

import com.zerobase.stockservice.dto.CompanyDto;
import com.zerobase.stockservice.dto.ScrapedResult;

import java.util.Optional;

public interface Scraper {
    Optional<CompanyDto> scrapCompanyByTicker(String ticker);
    ScrapedResult scrap(CompanyDto company);
}
