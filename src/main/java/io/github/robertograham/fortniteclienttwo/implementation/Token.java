package io.github.robertograham.fortniteclienttwo.implementation;

import javax.json.JsonObject;
import javax.json.bind.adapter.JsonbAdapter;
import java.util.Objects;

final class Token {

    private final String accessToken;
    private final String refreshToken;

    private Token(JsonObject jsonObject) {
        accessToken = jsonObject.getString("access_token", null);
        refreshToken = jsonObject.getString("refresh_token", null);
    }

    String accessToken() {
        return accessToken;
    }

    String refreshToken() {
        return refreshToken;
    }

    @Override
    public String toString() {
        return "Token{" +
                "accessToken='" + accessToken + '\'' +
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
                Objects.equals(refreshToken, token.refreshToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessToken, refreshToken);
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
