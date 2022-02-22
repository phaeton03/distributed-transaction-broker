package com.example.financial_institution.logging.pojo;


import com.example.financial_institution.enums.JournalType;
import lombok.Value;

@Value
public class Journal {
    String value;

    JournalType type;

    Long serverDurationMs;

    String uri;
}