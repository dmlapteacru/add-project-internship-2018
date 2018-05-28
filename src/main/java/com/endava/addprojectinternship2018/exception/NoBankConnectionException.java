package com.endava.addprojectinternship2018.exception;

public class NoBankConnectionException extends Exception {

    private static final String MESSAGE = "No connection with bank";

    public NoBankConnectionException() {
        super(MESSAGE);
    }

}
