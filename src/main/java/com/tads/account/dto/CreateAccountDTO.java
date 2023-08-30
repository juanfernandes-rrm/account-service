package com.tads.account.dto;

import com.tads.account.model.status.StatusAccount;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateAccountDTO(
        @NotNull
        UUID customerId,
        @NotNull
        UUID managerId,
        @NotNull
        BigDecimal salary
) {}
