package com.tech.sv.calentra.auth_service.exceptions;

public class ContentLengthException extends ApiException {

    public ContentLengthException() {
        super("Request body is required");
    }
}
