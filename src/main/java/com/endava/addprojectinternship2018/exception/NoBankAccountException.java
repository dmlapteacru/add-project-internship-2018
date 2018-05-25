package com.endava.addprojectinternship2018.exception;

public class NoBankAccountException extends Exception {

    private static final String MESSAGE = "User has't bank account";

    public NoBankAccountException() {
        super(MESSAGE);
    }

}
