package com.example.correspodentbank.enums;

public enum JournalType {
    FIN_INST("FIN_INST"),
    CLIENT("CLIENT"),
    BANK_CORR("BANK_CORR");


    private final String code;

    JournalType(String s) {
        code = s;
    }
}