package org.qazcodenarxoz.entity;


import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.OffsetDateTime;


@Data
@Entity
@Immutable
@Subselect("SELECT * FROM transaction_report")
public class TransactionReport {
    @Id
    @Column(name = "tx_id")
    private String txId;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "finished_at")
    private OffsetDateTime finishedAt;

    @Column(name = "status")
    private String status;

    @Column(name = "success")
    private Boolean success;

    @Column(name = "result_code")
    private String resultCode;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "currency")
    private String currency;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_category")
    private String productCategory;

    @Column(name = "merchant_code")
    private String merchantCode;

    @Column(name = "merchant_name")
    private String merchantName;

    @Column(name = "merchant_mcc")
    private String merchantMcc;

    @Column(name = "merchant_country")
    private String merchantCountry;

    @Column(name = "buyer_external_id")
    private String buyerExternalId;

    @Column(name = "buyer_email")
    private String buyerEmail;

    @Column(name = "buyer_full_name")
    private String buyerFullName;

    @Column(name = "agent_code")
    private String agentCode;

    @Column(name = "agent_name")
    private String agentName;

    @Column(name = "agent_type")
    private String agentType;

    @Column(name = "fee_calc_type")
    private String feeCalcType;

    @Column(name = "fee_rule_currency")
    private String feeRuleCurrency;

    @Column(name = "fee_rate")
    private BigDecimal feeRate;

    @Column(name = "fee_fixed_amount")
    private BigDecimal feeFixedAmount;

    @Column(name = "fee_min_amount")
    private BigDecimal feeMinAmount;

    @Column(name = "fee_max_amount")
    private BigDecimal feeMaxAmount;

    @Column(name = "commission_amount")
    private BigDecimal commissionAmount;

    @Column(name = "commission_rule_status")
    private String commissionRuleStatus;
}
