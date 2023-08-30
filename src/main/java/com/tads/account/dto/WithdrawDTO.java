package com.tads.account.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record WithdrawDTO(
        @NotNull @Min(value = 0L, message = "The value must be positive")
        BigDecimal amount
) {
}
