package com.tech.sv.calentra.auth_service.strategies.Jwt.impl;

import com.tech.sv.calentra.auth_service.exceptions.ContentLengthException;
import com.tech.sv.calentra.auth_service.strategies.Jwt.ValidationRuleStrategy;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class ContentLengthValidationStrategy implements ValidationRuleStrategy<HttpServletRequest> {

    @Override
    public void validate(HttpServletRequest request) {
        if (request.getContentLength() <= 0) {
            throw new ContentLengthException();
        }
    }
}
