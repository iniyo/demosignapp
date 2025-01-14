package com.example.demosignapp.infrastructure;

public enum OAuth2Provider {
    GOOGLE, KAKAO, NAVER;

    public static OAuth2Provider from(String registrationId) {
        try {
            return OAuth2Provider.valueOf(registrationId.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UnsupportedOperationException("Unsupported OAuth2 provider: " + registrationId);
        }
    }
}
