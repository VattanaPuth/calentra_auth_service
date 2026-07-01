package com.tech.sv.calentra.auth_service.strategies.JwtValidateStrategy;

public interface TokenExtractorStrategy<T, R> {
    R extract(T source);
}
