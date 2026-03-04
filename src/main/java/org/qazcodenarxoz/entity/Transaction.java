package org.qazcodenarxoz.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    private UUID id;

    @Column(unique = true, nullable = false)
    private String txId;

    private OffsetDateTime createdAt;
    private OffsetDateTime finishedAt;

    @ManyToOne
    @JoinColumn(name = "buyer_user_id", nullable = false)
    private User buyer;

    @ManyToOne
    @JoinColumn(name = "merchant_id", nullable = false)
    private Merchant merchant;

    @ManyToOne
    @JoinColumn(name = "agent_id", nullable = false)
    private Agent agent;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private BigDecimal amount;
    private String currency;
    private String status;
    private Boolean success;
    private String resultCode;
}