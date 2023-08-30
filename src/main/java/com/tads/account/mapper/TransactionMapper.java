package com.tads.account.mapper;

import com.tads.account.dto.ResponseTransactionDTO;
import com.tads.account.model.Transaction;
import com.tads.account.model.TransactionType;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public ResponseTransactionDTO TransactionToResponseTransactionDTO(Transaction transaction){
        Long destinationAccountId = null;
        if (transaction.getType() == TransactionType.TRANSFER) {
            destinationAccountId = transaction.getDestinationAccountId();
        }

        return new ResponseTransactionDTO(
                transaction.getId(),
                transaction.getDateTime(),
                transaction.getType(),
                transaction.getSourceAccountId(),
                destinationAccountId,
                transaction.getAmount()
        );
    }

}
