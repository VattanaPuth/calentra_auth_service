package com.tech.sv.calentra.auth_service.strategy.JwtValidateRule;

public interface TokenExtractor<T, R> {
    R extract(T source);
}
