package com.tads.account.repository;

import com.tads.account.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t WHERE t.sourceAccountId = :accountId AND t.dateTime >= :startDate AND t.dateTime <= :endDate")
    List<Transaction> findAllBySourceAccountIdAndDateTimeBetween(
            @Param("accountId") Long accountId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT t FROM Transaction t WHERE t.destinationAccountId = :accountId AND t.dateTime >= :startDate AND t.dateTime <= :endDate")
    List<Transaction> findAllByDestinationAccountIdAndDateTimeBetween(
            @Param("accountId") Long accountId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
