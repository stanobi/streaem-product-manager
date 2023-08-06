package com.streaem.productmanager.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponseDTO extends ResponseDTO {

    public ErrorResponseDTO() {
        super(false, "unable to process request", null);
    }

    public ErrorResponseDTO(String message) {
        super(false, message, null);
    }
}
