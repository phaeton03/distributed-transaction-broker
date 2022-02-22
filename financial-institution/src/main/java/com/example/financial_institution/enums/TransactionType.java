package com.example.financial_institution.enums;

public enum TransactionType {

    DEPOSIT("deposit"),
    WITHDRAWAL("withdrawal"),
    CREDIT("credit"),
    DEBIT("debit"),
    LOAN("loan");

    TransactionType(String type) {
        this.TYPE = type;
    }

    private final String TYPE;
}