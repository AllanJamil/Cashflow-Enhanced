package com.project.cashflow.exception;

public class TooManyMembersException extends Exception{

    public TooManyMembersException() {
        super("Maximum amount of members reached");
    }
}
