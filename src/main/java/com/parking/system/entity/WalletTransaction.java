package com.parking.system.entity;

import com.parking.system.enums.TransactionType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "wallet_transactions")
public class WalletTransaction {

    @Id
    private String id;

    @DBRef
    private User user;

    private BigDecimal amount;
    private TransactionType type;
    private LocalDateTime transactionDate = LocalDateTime.now();
    private String remarks;

    // Constructors
    public WalletTransaction() {}

    // Builder
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String id;
        private User user;
        private BigDecimal amount;
        private TransactionType type;
        private String remarks;

        public Builder id(String id) { this.id = id; return this; }
        public Builder user(User user) { this.user = user; return this; }
        public Builder amount(BigDecimal amount) { this.amount = amount; return this; }
        public Builder type(TransactionType type) { this.type = type; return this; }
        public Builder remarks(String remarks) { this.remarks = remarks; return this; }

        public WalletTransaction build() {
            WalletTransaction wt = new WalletTransaction();
            wt.id = this.id;
            wt.user = this.user;
            wt.amount = this.amount;
            wt.type = this.type;
            wt.remarks = this.remarks;
            wt.transactionDate = LocalDateTime.now();
            return wt;
        }
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }
    public LocalDateTime getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDateTime transactionDate) { this.transactionDate = transactionDate; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}
