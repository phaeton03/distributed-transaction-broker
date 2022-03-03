package ru.otus.spring.bank.logging.pojo;


import lombok.Value;
import ru.otus.spring.bank.enums.JournalType;

@Value
public class Journal {
    String value;

    JournalType type;

    Long serverDurationMs;

    String uri;
}