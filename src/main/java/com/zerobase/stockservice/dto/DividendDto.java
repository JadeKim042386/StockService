package com.zerobase.stockservice.dto;

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

}
