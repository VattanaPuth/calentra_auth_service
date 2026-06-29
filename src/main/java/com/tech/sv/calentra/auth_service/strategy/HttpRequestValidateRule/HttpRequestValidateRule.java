package com.tech.sv.calentra.auth_service.strategy.HttpRequestValidateRule;

public interface HttpRequestValidateRule<T> {
    void validate(T request);
}
