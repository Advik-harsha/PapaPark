package com.parking.system.service;

import com.parking.system.dto.WalletTransactionDto;

import java.math.BigDecimal;
import java.util.List;

public interface WalletService {
    WalletTransactionDto addMoney(String userId, BigDecimal amount);
    WalletTransactionDto deductMoney(String userId, BigDecimal amount, String remarks);
    BigDecimal getWalletBalance(String userId);
    List<WalletTransactionDto> getWalletHistory(String userId);
}
