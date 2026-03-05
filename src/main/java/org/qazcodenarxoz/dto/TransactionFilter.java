package org.qazcodenarxoz.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Data
public class TransactionFilter {
    private OffsetDateTime from;
    private OffsetDateTime to;
    private String merchantCode;
    private String agentCode;
    private String productCode;
    private String buyerEmail;
    private Boolean success;
    private List<String> status;
    private List<String> resultCode;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private String currency;
    private BigDecimal minCommission;
    private BigDecimal maxCommission;
    private Boolean hasFeeRule;
    private int page = 0;
    private int size = 20;
    private String sort = "createdAt,desc";
}