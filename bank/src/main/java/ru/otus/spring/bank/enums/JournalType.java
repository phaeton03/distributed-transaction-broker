package ru.otus.spring.bank.enums;

public enum JournalType {
    ADMIN("ADMIN"),
    CLIENT("CLIENT"),
    BANK("BANK"),
    DISTRIBURED_TRANSACTION("DISTRIBUTED_TRANSACTION");


    private final String code;

    JournalType(String s) {
        code = s;
    }
}