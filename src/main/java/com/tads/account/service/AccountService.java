package com.tads.account.service;

import com.tads.account.dto.*;
import com.tads.account.mapper.AccountMapper;
import com.tads.account.model.Account;
import com.tads.account.model.status.Pending;
import com.tads.account.repository.AccountRepository;
import com.tads.account.repository.StatusAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class AccountService {

    private static final BigDecimal VALUE_TO_GET_LIMIT = new BigDecimal(2000);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private StatusAccountRepository statusAccountRepository;
    @Autowired
    private AccountMapper accountMapper;

    @Transactional
    public ResponseCreateAccountDTO createAccount(CreateAccountDTO createAccountDTO) {
        Account account = accountMapper.createAccountDTOToAccount(createAccountDTO);
        initializeAccount(account);
        statusAccountRepository.saveAndFlush(account.getStatusAccount());
        return accountMapper.accountToResponseCreateAccountDTO(accountRepository.saveAndFlush(account));
    }

    //    @Transactional(readOnly = true)
    public ResponseCreateAccountDTO getAccount(Long id) {
        return accountMapper.accountToResponseCreateAccountDTO(accountRepository.findById(id).orElseThrow());
    }

    public ResponseCreateAccountDTO updateAccount(UpdateAccountDTO updateAccountDTO) {
        Account account = accountRepository.findById(updateAccountDTO.id()).orElseThrow();
        adjustLimitBySalary(account);
        return accountMapper.accountToResponseCreateAccountDTO(account);
    }

    @Transactional
    public ResponseTransactionDTO deposit(Long id, DepositDTO depositDTO) {
        Account account = accountRepository.findById(id).orElseThrow();
        depositAmount(account, depositDTO.amount());

        if (account.getBalance().compareTo(BigDecimal.ZERO) >= 0) {
            adjustLimitBySalary(account);
        }

        accountRepository.saveAndFlush(account);
        return transactionService.saveTransaction(id, depositDTO);
    }

    @Transactional
    public ResponseTransactionDTO withdraw(Long id, WithdrawDTO withdrawDTO) {
        Account account = accountRepository.findById(id).orElseThrow();
        withdrawAmount(account, withdrawDTO.amount());
        accountRepository.saveAndFlush(account);
        return transactionService.saveTransaction(id, withdrawDTO);
    }

    @Transactional
    public ResponseTransactionDTO transfer(Long id, TransferDTO transferDTO) {
        Account sourceAccount = accountRepository.findById(id).orElseThrow();
        Account destinationAccount = accountRepository.findById(transferDTO.destinationAccountId()).orElseThrow();

        transferAmount(sourceAccount, destinationAccount, transferDTO.amount());
        accountRepository.saveAndFlush(sourceAccount);
        accountRepository.saveAndFlush(destinationAccount);
        return transactionService.saveTransaction(id, transferDTO);
    }

    public ResponseBankStatementDTO getBankStatement(Long id, LocalDateTime startDate, LocalDateTime endDate) {
        Account account = accountRepository.findById(id).orElseThrow();
        return transactionService.findTransactionsByAccountAndDateBetween(account, startDate, endDate);
    }

    private void initializeAccount(Account account) {
        account.setId(new Random().nextLong());
        account.setBalance(BigDecimal.ZERO);
        adjustLimitBySalary(account);
        account.setStatusAccount(new Pending());
    }

    private void adjustLimitBySalary(Account account) {
        BigDecimal salary = account.getSalary();

        if (salary.compareTo(VALUE_TO_GET_LIMIT) >= 0) {
            BigDecimal limit = salary.divide(BigDecimal.valueOf(2));
            BigDecimal balance = account.getBalance();
            account.setAccountLimit(limit);

            if (balance.compareTo(BigDecimal.ZERO) < 0 && balance.abs().compareTo(limit) > 0) {
                account.setAccountLimit(BigDecimal.ZERO);
            }
        } else {
            account.setAccountLimit(BigDecimal.ZERO);
        }
    }

    private void adjustLimitToTransactions(Account account, BigDecimal amount) {
        BigDecimal balance = account.getBalance();
        BigDecimal limit = account.getAccountLimit();

        BigDecimal remainingBalance = balance.subtract(amount);

        if (remainingBalance.compareTo(BigDecimal.ZERO) < 0) {
            BigDecimal deficitAmount = amount.subtract(balance);
            BigDecimal newAccountLimit = limit.subtract(deficitAmount);
            account.setAccountLimit(newAccountLimit);
        }
    }

    private void depositAmount(Account account, BigDecimal amount) {
        BigDecimal balance = account.getBalance();
        account.setBalance(balance.add(amount));
    }

    private void withdrawAmount(Account account, BigDecimal amount) {
        BigDecimal balance = account.getBalance();
        adjustLimitToTransactions(account, amount);
        account.setBalance(balance.subtract(amount));
    }

    private void transferAmount(Account sourceAccount, Account destinationAccount, BigDecimal amount) {
        BigDecimal sourceAccountBalance = sourceAccount.getBalance();
        BigDecimal sourceAccountBalanceWithLimit = sourceAccountBalance.add(sourceAccount.getAccountLimit());
        BigDecimal destinationAccountBalance = destinationAccount.getBalance();

        if (amount.compareTo(sourceAccountBalanceWithLimit) <= 0) {
            BigDecimal remainingBalance = sourceAccountBalance.subtract(amount);
            adjustLimitToTransactions(sourceAccount, amount);

            sourceAccount.setBalance(remainingBalance);
            destinationAccount.setBalance(destinationAccountBalance.add(amount));
        } else {
            throw new RuntimeException("Insufficient funds for transfer");
        }
    }
}
