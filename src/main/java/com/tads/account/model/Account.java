package com.tads.account.model;

import com.tads.account.model.status.StatusAccount;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal salary;
    private BigDecimal accountLimit;
    private BigDecimal balance;
    @ManyToOne
    @JoinColumn(name = "status_account_id", foreignKey = @ForeignKey(name = "fk_account_status"))
    private StatusAccount statusAccount;
    @Column(unique = true)
    private UUID customerId;
    private UUID managerId;
}
