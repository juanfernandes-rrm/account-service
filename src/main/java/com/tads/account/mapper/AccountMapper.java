package com.tads.account.mapper;

import com.tads.account.dto.CreateAccountDTO;
import com.tads.account.dto.ResponseCreateAccountDTO;
import com.tads.account.model.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public Account createAccountDTOToAccount(CreateAccountDTO createAccountDTO) {
        return Account.builder()
                .customerId(createAccountDTO.customerId())
                .managerId(createAccountDTO.managerId())
                .salary(createAccountDTO.salary())
                .build();
    }

    public ResponseCreateAccountDTO accountToResponseCreateAccountDTO(Account account) {
        return new ResponseCreateAccountDTO(
                account.getId(),
                account.getCustomerId(),
                account.getManagerId(),
                account.getSalary(),
                account.getAccountLimit(),
                account.getBalance(),
                account.getStatusAccount().getClass().getName());
    }
}
