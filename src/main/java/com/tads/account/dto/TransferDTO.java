package com.tads.account.dto;

import com.tads.account.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransferDTO(
        Long destinationAccountId,
        BigDecimal amount
)
{}
