package ru.otus.spring.bank.controller.dto.request;

import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Value
public class PeriodRequest {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate start;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate end;
}