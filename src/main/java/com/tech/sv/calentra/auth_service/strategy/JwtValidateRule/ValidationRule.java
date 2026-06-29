package com.tech.sv.calentra.auth_service.strategy.JwtValidateRule;

public interface ValidationRule<T> {
    void validate(T request);
}
