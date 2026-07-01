package com.tech.sv.calentra.auth_service.strategies.JwtValidateStrategy;

public interface ValidationRuleStrategy<T> {
    void validate(T request);
}
