package com.tads.account.dto;

import com.tads.account.model.status.StatusAccount;
import jakarta.persistence.Column;

import java.math.BigDecimal;
import java.util.UUID;

public record ResponseCreateAccountDTO(
        Long id,
        UUID customerId,
        UUID managerId,
        BigDecimal salary,
        BigDecimal limit,
        BigDecimal balance,
        String status
) {}
