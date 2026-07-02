package com.tech.sv.calentra.auth_service.dtos.records;

public record OAuth2UserInfo(
        String providerUserId,
        String email,
        String name,
        String avatarUrl,
        boolean emailVerified
) {}
