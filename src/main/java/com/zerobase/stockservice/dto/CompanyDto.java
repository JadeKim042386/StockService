package com.zerobase.stockservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CompanyDto {
    private String ticker;
    private String name;
}
