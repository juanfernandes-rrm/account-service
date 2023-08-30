package com.tads.account.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tads.account.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

public record ResponseTransactionDTO(
        Long id,
        LocalDateTime dateTime,
        TransactionType type,
        Long sourceAccountId,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Long destinationAccountId,
        BigDecimal amount
)
{}
