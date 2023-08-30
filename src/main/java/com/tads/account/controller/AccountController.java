package com.tads.account.controller;

import com.tads.account.dto.*;
import com.tads.account.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping
    public ResponseEntity<EntityModel<ResponseCreateAccountDTO>> createAccount(@RequestBody @Valid CreateAccountDTO createAccountDTO) {
        ResponseCreateAccountDTO account = accountService.createAccount(createAccountDTO);

        EntityModel<ResponseCreateAccountDTO> accountResource = EntityModel.of(account);
        accountResource.add(linkTo(methodOn(AccountController.class).getAccount(account.id())).withSelfRel());

        return ResponseEntity.created(URI.create(accountResource.getRequiredLink(IanaLinkRelations.SELF).getHref())).body(accountResource);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ResponseCreateAccountDTO>> getAccount(@PathVariable Long id) {
        ResponseCreateAccountDTO account = accountService.getAccount(id);

        EntityModel<ResponseCreateAccountDTO> accountResource = EntityModel.of(account);
        accountResource.add(linkTo(methodOn(AccountController.class).getAccount(account.id())).withSelfRel());

        return ResponseEntity.ok(accountResource);
    }

    @PutMapping
    public ResponseEntity<EntityModel<ResponseCreateAccountDTO>> updateAccount(@RequestBody @Valid UpdateAccountDTO updateAccountDTO) {
        ResponseCreateAccountDTO account = accountService.updateAccount(updateAccountDTO);

        EntityModel<ResponseCreateAccountDTO> accountResource = EntityModel.of(account);
        accountResource.add(linkTo(methodOn(AccountController.class).getAccount(account.id())).withSelfRel());

        return ResponseEntity.created(URI.create(accountResource.getRequiredLink(IanaLinkRelations.SELF).getHref())).body(accountResource);
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<EntityModel<ResponseTransactionDTO>> deposit(@PathVariable Long id, @RequestBody @Valid DepositDTO depositDTO) {
        ResponseTransactionDTO deposit = accountService.deposit(id, depositDTO);

        EntityModel<ResponseTransactionDTO> depositResource = EntityModel.of(deposit);
        //TODO: get transaction
        depositResource.add(linkTo(methodOn(AccountController.class).getAccount(deposit.id())).withSelfRel());
        depositResource.add(linkTo(methodOn(AccountController.class).withdraw(deposit.id(), new WithdrawDTO(BigDecimal.ZERO))).withSelfRel());

        return ResponseEntity.ok(depositResource);
    }

    @PostMapping("/{id}/withdraw")
    public ResponseEntity<EntityModel<ResponseTransactionDTO>> withdraw(@PathVariable Long id, @RequestBody @Valid WithdrawDTO withdrawDTO) {
        ResponseTransactionDTO withdraw = accountService.withdraw(id, withdrawDTO);

        EntityModel<ResponseTransactionDTO> withdrawResource = EntityModel.of(withdraw);
        //TODO: get transaction
        withdrawResource.add(linkTo(methodOn(AccountController.class).getAccount(withdraw.id())).withSelfRel());
        withdrawResource.add(linkTo(methodOn(AccountController.class).deposit(withdraw.id(), new DepositDTO(BigDecimal.ZERO))).withSelfRel());

        return ResponseEntity.ok(withdrawResource);
    }

    @PostMapping("/{id}/transfer")
    public ResponseEntity<EntityModel<ResponseTransactionDTO>> transfer(@PathVariable Long id,
                                                                        @RequestBody @Valid TransferDTO transferDTO) {
        ResponseTransactionDTO transfer = accountService.transfer(id, transferDTO);

        EntityModel<ResponseTransactionDTO> transferResource = EntityModel.of(transfer);
        //TODO: get transaction
        transferResource.add(linkTo(methodOn(AccountController.class).getAccount(transfer.id())).withSelfRel());
        transferResource.add(linkTo(methodOn(AccountController.class).deposit(transfer.id(), new DepositDTO(BigDecimal.ZERO))).withSelfRel());

        return ResponseEntity.ok(transferResource);
    }

    @GetMapping("{id}/statement")
    public ResponseEntity<EntityModel<ResponseBankStatementDTO>> statement(
            @PathVariable Long id,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        ResponseBankStatementDTO statement = accountService.getBankStatement(id, startDate, endDate);
        EntityModel<ResponseBankStatementDTO> statementResource = EntityModel.of(statement);

        return ResponseEntity.ok(statementResource);
    }
}
