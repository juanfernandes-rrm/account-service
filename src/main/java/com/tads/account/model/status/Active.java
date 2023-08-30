package com.tads.account.model.status;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ACTIVE")
public class Active extends StatusAccount{

}
