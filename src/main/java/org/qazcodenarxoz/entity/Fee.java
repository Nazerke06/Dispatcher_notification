package org.qazcodenarxoz.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "fee")
public class Fee {
    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "merchant_id", nullable = false)
    private Merchant merchant;

    @ManyToOne
    @JoinColumn(name = "agent_id", nullable = false)
    private Agent agent;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private String currency; // KZT, USD...
    private String calcType; // PERCENT, FIXED
    private BigDecimal rate;
    private BigDecimal fixedAmount;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
}