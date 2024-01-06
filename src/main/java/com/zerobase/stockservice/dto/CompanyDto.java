package com.zerobase.stockservice.dto;

import com.zerobase.stockservice.domain.Company;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CompanyDto {
    private String ticker;
    private String name;

    public Company toEntity() {
        return Company.builder()
                .ticker(ticker)
                .name(name)
                .build();
    }
}
