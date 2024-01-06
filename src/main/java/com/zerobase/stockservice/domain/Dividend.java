package com.zerobase.stockservice.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "DIVIDEND")
@Table(
        uniqueConstraints = {
        @UniqueConstraint(
                columnNames = {"companyId", "date"}
        )
    }
)
public class Dividend {
    @Id
    @GeneratedValue
    private Long id;
    private Long companyId;
    private LocalDateTime date;
    private String dividend;
}
