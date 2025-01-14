package com.example.demosignapp.domain.token;


import java.util.Optional;

public interface RefreshTokenRepository {

    void saveOrUpdate(RefreshToken token);

    Optional<RefreshToken> findByAccessToken(String accessToken);

    void deleteAllByMemberKey(String memberKey);

    void deleteByAccessToken(String accessToken);
}
