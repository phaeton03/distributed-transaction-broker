package ru.otus.spring.bank.enums;

public enum Status {
    SUCCESS("success"), FAIL("fail"), IN_PROGRESS("in progress"), COMMIT("commit");

    Status(String status) {
        this.STATUS = status;
    }

    private final String STATUS;
}