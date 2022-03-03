package com.example.correspodentbank.logging.pojo;


import com.example.correspodentbank.enums.JournalType;
import lombok.Value;

@Value
public class Journal {
    String value;

    JournalType type;

    Long serverDurationMs;

    String uri;
}