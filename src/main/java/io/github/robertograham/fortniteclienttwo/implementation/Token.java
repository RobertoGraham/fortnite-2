package io.github.robertograham.fortniteclienttwo.implementation;

import javax.json.JsonObject;
import javax.json.bind.adapter.JsonbAdapter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.Optional;

final class Token {

    private final String accessToken;
    private final LocalDateTime expiresAt;
    private final String refreshToken;

    private Token(JsonObject jsonObject) {
        accessToken = jsonObject.getString("access_token", null);
        expiresAt = Optional.ofNullable(jsonObject.getString("expires_at", null))
                .map(expiresAt ->
                        LocalDateTime.ofInstant(
                                Instant.parse(expiresAt),
                                ZoneOffset.UTC
                        )
                )
                .orElse(null);
        refreshToken = jsonObject.getString("refresh_token", null);
    }

    String accessToken() {
        return accessToken;
    }

    String refreshToken() {
        return refreshToken;
    }

    boolean isExpired() {
        return Optional.ofNullable(expiresAt)
                .map(LocalDateTime.now()::isAfter)
                .orElse(true);
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
        return Objects.equals(accessToken, token.accessToken) &&
                Objects.equals(expiresAt, token.expiresAt) &&
                Objects.equals(refreshToken, token.refreshToken);
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
            return new Token(jsonObject);
        }
    }
}
