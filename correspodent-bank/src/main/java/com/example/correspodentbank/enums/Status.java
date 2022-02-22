package com.example.correspodentbank.enums;

public enum Status {
    SUCCESS("success"),
    COMMIT("commit"),
    FAIL("fail"),
    IN_PROGRESS("in_progress");

    Status(String status) {
        this.STATUS = status;
    }

    private final String STATUS;
}