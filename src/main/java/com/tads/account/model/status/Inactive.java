package com.tads.account.model.status;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("INACTIVE")
public class Inactive extends StatusAccount{

}
