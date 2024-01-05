package com.zerobase.stockservice.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "DIVIDEND")
public class Dividend {
    @Id
    @GeneratedValue
    private Long id;
    private Long companyId;
    private LocalDateTime date;
    private String dividend;
}
