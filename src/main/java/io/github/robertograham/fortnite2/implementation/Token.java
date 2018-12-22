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

    private Token(String accessToken,
                  LocalDateTime expiresAt,
                  String refreshToken) {
        this.accessToken = accessToken;
        this.expiresAt = expiresAt;
        this.refreshToken = refreshToken;
    }

    String accessToken() {
        return accessToken;
    }

    String refreshToken() {
        return refreshToken;
    }

    boolean isExpired() {
        return LocalDateTime.now()
                .isAfter(expiresAt);
    }

    @Override
    public String toString() {
        return "Token{" +
                "accessToken='" + accessToken + '\'' +
                ", expiresAt=" + expiresAt +
                ", refreshToken='" + refreshToken + '\'' +
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
                refreshToken.equals(token.refreshToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessToken, expiresAt, refreshToken);
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
                    jsonObject.getString("refresh_token")
            );
        }
    }
}
