package com.tech.sv.calentra.auth_service.strategy.JwtValidateStrategy;

public interface ValidationRule<T> {
    void validate(T request);
}
