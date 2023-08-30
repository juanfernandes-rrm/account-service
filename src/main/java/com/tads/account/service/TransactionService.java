package com.tads.account.service;

import com.tads.account.dto.*;
import com.tads.account.mapper.TransactionMapper;
import com.tads.account.model.Account;
import com.tads.account.model.Transaction;
import com.tads.account.model.TransactionType;
import com.tads.account.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionMapper transactionMapper;

    public ResponseTransactionDTO saveTransaction(Long accountId, DepositDTO depositDTO) {
        Transaction transaction = Transaction.builder()
                .dateTime(LocalDateTime.now())
                .type(TransactionType.DEPOSIT)
                .sourceAccountId(accountId)
                .amount(depositDTO.amount())
                .build();
        return transactionMapper.TransactionToResponseTransactionDTO(transactionRepository.saveAndFlush(transaction));
    }

    public ResponseTransactionDTO saveTransaction(Long accountId, WithdrawDTO withdrawDTO) {
        Transaction transaction = Transaction.builder()
                .dateTime(LocalDateTime.now())
                .type(TransactionType.WITHDRAWAL)
                .sourceAccountId(accountId)
                .amount(withdrawDTO.amount())
                .build();
        return transactionMapper.TransactionToResponseTransactionDTO(transactionRepository.saveAndFlush(transaction));
    }

    public ResponseTransactionDTO saveTransaction(Long accountId, TransferDTO transferDTO) {
        Transaction transaction = Transaction.builder()
                .dateTime(LocalDateTime.now())
                .type(TransactionType.TRANSFER)
                .sourceAccountId(accountId)
                .destinationAccountId(transferDTO.destinationAccountId())
                .amount(transferDTO.amount())
                .build();
        return transactionMapper.TransactionToResponseTransactionDTO(transactionRepository.saveAndFlush(transaction));
    }

    public ResponseBankStatementDTO findTransactionsByAccountAndDateBetween(Account account, LocalDateTime startDate, LocalDateTime endDate) {
        List<Transaction> allBySourceAccountId =
                transactionRepository.findAllBySourceAccountIdAndDateTimeBetween(account.getId(), startDate, endDate);
        List<Transaction> allByDestinationAccountId =
                transactionRepository.findAllByDestinationAccountIdAndDateTimeBetween(account.getId(), startDate, endDate);

        List<ResponseTransactionDTO> transactionSourceDTOList = allBySourceAccountId.stream()
                .map(transaction -> transactionMapper.TransactionToResponseTransactionDTO(transaction))
                .collect(Collectors.toList());
        List<ResponseTransactionDTO> transactionDestinationDTOList = allByDestinationAccountId.stream()
                .map(transaction -> transactionMapper.TransactionToResponseTransactionDTO(transaction))
                .collect(Collectors.toList());

        List<ResponseTransactionDTO> transactionDTOList = new ArrayList<>();
        transactionDTOList.addAll(transactionSourceDTOList);
        transactionDTOList.addAll(transactionDestinationDTOList);

        return new ResponseBankStatementDTO(startDate, endDate, transactionDTOList);
    }

}
