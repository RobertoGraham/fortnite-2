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

    LocalDateTime expiresAt() {
        return expiresAt;
    }

    String refreshToken() {
        return refreshToken;
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
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Token))
            return false;
        Token that = (Token) o;
        return Objects.equals(accessToken, that.accessToken) &&
                Objects.equals(expiresAt, that.expiresAt) &&
                Objects.equals(refreshToken, that.refreshToken);
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
