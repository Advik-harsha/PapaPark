package com.parking.system.dto;

import com.parking.system.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class WalletTransactionDto {
    private String id;
    private String userId;
    private BigDecimal amount;
    private TransactionType type;
    private LocalDateTime transactionDate;
    private String remarks;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }
    public LocalDateTime getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDateTime transactionDate) { this.transactionDate = transactionDate; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}
