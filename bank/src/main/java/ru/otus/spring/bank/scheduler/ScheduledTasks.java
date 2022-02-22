package ru.otus.spring.bank.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import ru.otus.spring.bank.domain.Transaction;
import ru.otus.spring.bank.enums.Status;
import ru.otus.spring.bank.service.TransactionService;

import java.util.List;

@EnableScheduling
@Configuration
@RequiredArgsConstructor
public class ScheduledTasks {
    private final TransactionService transactionService;

    @Scheduled(fixedDelayString = "${schedule-task.transactions.fixedDelay.bank}")
    public void scheduleCommitTransaction() {
        transactionService.calculateCommitTransactions();
    }
}