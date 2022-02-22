package com.example.financial_institution.scheduler;

import com.example.financial_institution.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@Configuration
@RequiredArgsConstructor
public class ScheduledTasks {
    private final TransactionService transactionService;

    @Scheduled(fixedDelayString = "${schedule-task.transactions.financial-institution.fixedDelay}")
    public void scheduleCommitBankTransactions() {
        transactionService.calculateCommitTransactions();
    }

}