package com.zerobase.stockservice.dto;

import com.zerobase.stockservice.domain.Company;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDto {
    private String ticker;
    private String name;

    public static CompanyDto of(String ticker, String name) {
        return new CompanyDto(ticker, name);
    }

    public Company toEntity() {
        return Company.builder()
                .ticker(ticker)
                .name(name)
                .build();
    }

    public static CompanyDto fromEntity(Company company) {
        return CompanyDto.of(company.getTicker(), company.getName());
    }
}
