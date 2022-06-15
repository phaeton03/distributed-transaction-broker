package ru.otus.example.logging.pojo;


import lombok.Value;
import ru.otus.example.enums.JournalType;

@Value
public class Journal {
    String value;

    JournalType type;

    Long serverDurationMs;

    String uri;
}