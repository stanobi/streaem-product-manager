package com.streaem.productmanager.exception;

public class StreaemRuntimeException extends Exception {

    public StreaemRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public StreaemRuntimeException(String message) {
        super(message);
    }

}
