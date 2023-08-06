package com.streaem.productmanager.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class StreaemException extends Exception {

    private final HttpStatus httpStatus;

    public StreaemException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
