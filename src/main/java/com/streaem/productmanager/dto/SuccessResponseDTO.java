package com.streaem.productmanager.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuccessResponseDTO extends ResponseDTO {

    public SuccessResponseDTO(Object data) {
        super(true, "processed successfully", data);
    }

    public SuccessResponseDTO(String message, Object data) {
        super(true, message, data);
    }
}
