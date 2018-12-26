package io.github.robertograham.fortnite2.implementation;

import javax.json.JsonObject;
import javax.json.bind.adapter.JsonbAdapter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

final class Token {

    private final String accessToken;
    private final LocalDateTime expiresAt;
    private final String refreshToken;
    private final LocalDateTime refreshExpiresAt;
    private final String inAppId;

    private Token(String accessToken,
                  LocalDateTime expiresAt,
                  String refreshToken,
                  LocalDateTime refreshExpiresAt,
                  String inAppId) {
        this.accessToken = accessToken;
        this.expiresAt = expiresAt;
        this.refreshToken = refreshToken;
        this.refreshExpiresAt = refreshExpiresAt;
        this.inAppId = inAppId;
    }

    String accessToken() {
        return accessToken;
    }

    LocalDateTime expiresAt() {
        return expiresAt;
    }

    String refreshToken() {
        return refreshToken;
    }

    LocalDateTime refreshExpiresAt() {
        return refreshExpiresAt;
    }

    String inAppId() {
        return inAppId;
    }

    @Override
    public String toString() {
        return "Token{" +
                "accessToken='" + accessToken + '\'' +
                ", expiresAt=" + expiresAt +
                ", refreshToken='" + refreshToken + '\'' +
                ", refreshExpiresAt=" + refreshExpiresAt +
                ", inAppId='" + inAppId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (!(object instanceof Token))
            return false;
        Token token = (Token) object;
        return accessToken.equals(token.accessToken) &&
                expiresAt.equals(token.expiresAt) &&
                refreshToken.equals(token.refreshToken) &&
                refreshExpiresAt.equals(token.refreshExpiresAt) &&
                inAppId.equals(token.inAppId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessToken, expiresAt, refreshToken, refreshExpiresAt, inAppId);
    }

    enum Adapter implements JsonbAdapter<Token, JsonObject> {

        INSTANCE;

        @Override
        public JsonObject adaptToJson(Token token) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Token adaptFromJson(JsonObject jsonObject) {
            return new Token(
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
                    jsonObject.getString("in_app_id")
            );
        }
    }
}
