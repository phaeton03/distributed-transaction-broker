package com.example.correspodentbank.sheduler;

import com.example.correspodentbank.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@Configuration
@RequiredArgsConstructor
public class ScheduledTasks {
    private final TransactionService transactionService;

    @Scheduled(fixedDelayString = "${schedule-task.transactions.fixedDelay.bank}")
    public void scheduleCommitBankTransactions() {
        transactionService.calculateBankCommitTransactions();
    }

    @Scheduled(fixedDelayString = "${schedule-task.transactions.fixedDelay.financial-intermediary}")
    public void scheduleSuccessBankTransactionsToFinancialIntermediary() {
        transactionService.calculateSuccessBankTransactionsToFinancialInstitution();
    }

}