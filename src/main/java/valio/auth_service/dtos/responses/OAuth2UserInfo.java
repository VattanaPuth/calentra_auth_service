package valio.auth_service.dtos.responses;

public record OAuth2UserInfo(
        String providerUserId,
        String email,
        String name,
        String avatarUrl,
        Boolean emailVerified
) {}
