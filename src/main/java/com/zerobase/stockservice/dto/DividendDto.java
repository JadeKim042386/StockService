package com.zerobase.stockservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.zerobase.stockservice.domain.Company;
import com.zerobase.stockservice.domain.Dividend;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DividendDto {
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime date;
    private String dividend;

    public static DividendDto of(LocalDateTime date, String dividend) {
        return new DividendDto(date, dividend);
    }

    public Dividend toEntity(Company company) {
        return Dividend.builder()
                .company(company)
                .date(date)
                .dividend(dividend)
                .build();
    }

    public static DividendDto fromEntity(Dividend dividend) {
        return DividendDto.of(dividend.getDate(), dividend.getDividend());
    }
}
