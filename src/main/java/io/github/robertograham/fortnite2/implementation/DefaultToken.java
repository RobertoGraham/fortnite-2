package io.github.robertograham.fortnite2.implementation;

import io.github.robertograham.fortnite2.domain.Token;

import javax.json.JsonObject;
import javax.json.bind.adapter.JsonbAdapter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

final class DefaultToken implements Token {

    private final String accessToken;
    private final LocalDateTime accessTokenExpiryTime;
    private final String refreshToken;
    private final LocalDateTime refreshTokenExpiryTime;
    private final String inAppId;
    private final String accountId;

    private DefaultToken(final String accessToken,
                         final LocalDateTime accessTokenExpiryTime,
                         final String refreshToken,
                         final LocalDateTime refreshTokenExpiryTime,
                         final String inAppId,
                         final String accountId) {
        this.accessToken = accessToken;
        this.accessTokenExpiryTime = accessTokenExpiryTime;
        this.refreshToken = refreshToken;
        this.refreshTokenExpiryTime = refreshTokenExpiryTime;
        this.inAppId = inAppId;
        this.accountId = accountId;
    }

    @Override
    public String accessToken() {
        return accessToken;
    }

    @Override
    public LocalDateTime accessTokenExpiryTime() {
        return accessTokenExpiryTime;
    }

    @Override
    public String refreshToken() {
        return refreshToken;
    }

    @Override
    public LocalDateTime refreshTokenExpiryTime() {
        return refreshTokenExpiryTime;
    }

    @Override
    public String inAppId() {
        return inAppId;
    }

    @Override
    public String accountId() {
        return accountId;
    }

    @Override
    public String toString() {
        return "DefaultToken{" +
            "accessToken='" + accessToken + '\'' +
            ", accessTokenExpiryTime=" + accessTokenExpiryTime +
            ", refreshToken='" + refreshToken + '\'' +
            ", refreshTokenExpiryTime=" + refreshTokenExpiryTime +
            ", inAppId='" + inAppId + '\'' +
            ", accountId='" + accountId + '\'' +
            '}';
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object)
            return true;
        if (!(object instanceof DefaultToken))
            return false;
        final var defaultToken = (DefaultToken) object;
        return accessToken.equals(defaultToken.accessToken) &&
            accessTokenExpiryTime.equals(defaultToken.accessTokenExpiryTime) &&
            refreshToken.equals(defaultToken.refreshToken) &&
            refreshTokenExpiryTime.equals(defaultToken.refreshTokenExpiryTime) &&
            inAppId.equals(defaultToken.inAppId) &&
            accountId.equals(defaultToken.accountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessToken, accessTokenExpiryTime, refreshToken, refreshTokenExpiryTime, inAppId, accountId);
    }

    enum Adapter implements JsonbAdapter<DefaultToken, JsonObject> {

        INSTANCE;

        @Override
        public JsonObject adaptToJson(final DefaultToken defaultToken) {
            throw new UnsupportedOperationException();
        }

        @Override
        public DefaultToken adaptFromJson(final JsonObject jsonObject) {
            return new DefaultToken(
                jsonObject.getString("access_token"),
                LocalDateTime.ofInstant(
                    Instant.parse(jsonObject.getString("expires_at")),
                    ZoneOffset.UTC
                ),
                jsonObject.getString("refresh_token"),
                LocalDateTime.ofInstant(
                    Instant.parse(jsonObject.getString("refresh_expires_at")),
                    ZoneOffset.UTC
                ),
                jsonObject.getString("in_app_id"),
                jsonObject.getString("account_id")
            );
        }
    }
}
