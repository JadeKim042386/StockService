package com.zerobase.stockservice.dto;

import com.zerobase.stockservice.domain.Dividend;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class DividendDto {
    private LocalDateTime date;
    private String dividend;

    public Dividend toEntity(Long companyId) {
        return Dividend.builder()
                .companyId(companyId)
                .date(date)
                .dividend(dividend)
                .build();
    }
}
