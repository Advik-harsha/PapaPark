package com.parking.system.entity;

import com.parking.system.enums.Role;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "users")
public class User {

    @Id
    private String id;

    private String fullName;

    @Indexed(unique = true)
    private String email;

    @Indexed(unique = true)
    private String phone;

    private String password;

    private Role role;

    private BigDecimal walletBalance = BigDecimal.ZERO;

    private boolean enabled = true;

    private LocalDateTime createdAt = LocalDateTime.now();

    // Constructors
    public User() {}

    public User(String id, String fullName, String email, String phone, String password,
                Role role, BigDecimal walletBalance, boolean enabled) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role = role;
        this.walletBalance = walletBalance;
        this.enabled = enabled;
        this.createdAt = LocalDateTime.now();
    }

    // Builder
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String id;
        private String fullName;
        private String email;
        private String phone;
        private String password;
        private Role role;
        private BigDecimal walletBalance = BigDecimal.ZERO;
        private boolean enabled = true;

        public Builder id(String id) { this.id = id; return this; }
        public Builder fullName(String fullName) { this.fullName = fullName; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder phone(String phone) { this.phone = phone; return this; }
        public Builder password(String password) { this.password = password; return this; }
        public Builder role(Role role) { this.role = role; return this; }
        public Builder walletBalance(BigDecimal walletBalance) { this.walletBalance = walletBalance; return this; }
        public Builder enabled(boolean enabled) { this.enabled = enabled; return this; }

        public User build() {
            User u = new User();
            u.id = this.id;
            u.fullName = this.fullName;
            u.email = this.email;
            u.phone = this.phone;
            u.password = this.password;
            u.role = this.role;
            u.walletBalance = this.walletBalance;
            u.enabled = this.enabled;
            u.createdAt = LocalDateTime.now();
            return u;
        }
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public BigDecimal getWalletBalance() { return walletBalance; }
    public void setWalletBalance(BigDecimal walletBalance) { this.walletBalance = walletBalance; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
