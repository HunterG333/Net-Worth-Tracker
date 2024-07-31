package com.Greer.Financial_Tracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "finances")
public class FinancesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "finances_id_seq")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id")
    private PersonEntity userId;

    private LocalDate date;

    private BigDecimal cash;

    private BigDecimal assets;

    private BigDecimal investments;

    private BigDecimal debt;

    public BigDecimal getNetWorth() {
        return cash.add(assets).add(investments).subtract(debt);
    }
}
