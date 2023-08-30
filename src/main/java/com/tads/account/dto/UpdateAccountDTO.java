package com.tads.account.dto;

import java.math.BigDecimal;

public record UpdateAccountDTO(
        Long id,
        BigDecimal salary
)
{}
