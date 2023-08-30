package com.tads.account.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record ResponseBankStatementDTO(
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDateTime startDate,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDateTime endDate,
        List<ResponseTransactionDTO> transactionDTOList
) {
}
