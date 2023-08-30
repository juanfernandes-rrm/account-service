package com.tads.account.model.status;

import com.tads.account.model.Account;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PENDING")
public class Pending extends StatusAccount{
    @Override
    public void approve(Account account) {
        account.setStatusAccount(new Active());
    }

    @Override
    public void reprove(Account account) {
        account.setStatusAccount(new Inactive());
    }
}
