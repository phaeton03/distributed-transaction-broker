package com.example.financial_institution.enums;

public enum JournalType {
    ADMIN("ADMIN"),
    CLIENT("CLIENT"),
    TRANSACTION("TRANSACTION");


    private final String code;

    JournalType(String s) {
        code = s;
    }
}