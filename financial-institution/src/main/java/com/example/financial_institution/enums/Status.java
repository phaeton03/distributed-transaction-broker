package com.example.financial_institution.enums;

public enum Status {
    SUCCESS("success"),
    COMMIT("commit"),
    FAIL("fail"),
    IN_PROGRESS("in progress");

    Status(String status) {
        this.STATUS = status;
    }

    private final String STATUS;
}