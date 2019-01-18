package io.github.robertograham.fortnite2.implementation;

import javax.json.JsonObject;
import javax.json.bind.adapter.JsonbAdapter;
import java.util.Objects;

final class Exchange {

    private final String code;

    private Exchange(final String code) {
        this.code = code;
    }

    String code() {
        return code;
    }

    @Override
    public String toString() {
        return "Exchange{" +
            "code='" + code + '\'' +
            '}';
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object)
            return true;
        if (!(object instanceof Exchange))
            return false;
        final Exchange exchange = (Exchange) object;
        return Objects.equals(code, exchange.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    enum Adapter implements JsonbAdapter<Exchange, JsonObject> {

        INSTANCE;

        @Override
        public JsonObject adaptToJson(final Exchange exchange) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Exchange adaptFromJson(final JsonObject jsonObject) {
            return new Exchange(jsonObject.getString("code"));
        }
    }
}
