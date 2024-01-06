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

    public static DividendDto fromEntity(Dividend dividend) {
        return DividendDto.builder()
                .dividend(dividend.getDividend())
                .date(dividend.getDate())
                .build();
    }
}
