package io.github.robertograham.fortnite2.domain;

import java.time.LocalDateTime;

/**
 * Representation of an Epic Games authentication session
 *
 * @since 2.1.0
 */
public interface Token {

    /**
     * @return this instance's access token
     * @since 2.1.0
     */
    String accessToken();

    /**
     * @return the time that this instance's access token will no longer be valid after.
     * Usually 8 hours after this instance was created
     * @since 2.1.0
     */
    LocalDateTime accessTokenExpiryTime();

    /**
     * @return this instance's refresh token
     * @since 2.1.0
     */
    String refreshToken();

    /**
     * @return the time that this instance's refresh token will no longer be valid after.
     * Usually 24 hours after this instance was created
     * @since 2.1.0
     */
    LocalDateTime refreshTokenExpiryTime();

    /**
     * @return this instance's in-app ID. Don't know what this is but it has some uses
     * @since 2.1.0
     */
    String inAppId();

    /**
     * @return the ID of the authenticated user's account
     * @since 2.1.0
     */
    String accountId();
}
