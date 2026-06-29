package com.tech.sv.calentra.auth_service.strategy.JwtValidateStrategy;

public interface TokenExtractor<T, R> {
    R extract(T source);
}
