package com.tech.sv.calentra.auth_service.strategies.Jwt;

public interface TokenExtractorStrategy<T, R> {
    R extract(T source);
}
