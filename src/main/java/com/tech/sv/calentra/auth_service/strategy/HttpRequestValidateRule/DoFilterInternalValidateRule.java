package com.tech.sv.calentra.auth_service.strategy.HttpRequestValidateRule;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

public interface DoFilterInternalValidateRule {
    String authHeaderValidate(HttpServletRequest request);
}
