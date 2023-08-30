package com.tads.account.model.status;

import com.tads.account.exception.DomainException;
import com.tads.account.model.Account;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "status_type")
public abstract class StatusAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public void approve(Account account){
        throw new DomainException("Account cannot be approved");
    }

    public void reprove(Account account){
        throw new DomainException("Account cannot be reproved");
    }
}
