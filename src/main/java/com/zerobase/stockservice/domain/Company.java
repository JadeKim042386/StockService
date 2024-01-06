package com.zerobase.stockservice.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "COMPANY")
public class Company {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String ticker;
    private String name;
}
