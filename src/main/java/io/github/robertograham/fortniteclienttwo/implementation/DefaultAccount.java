package io.github.robertograham.fortniteclienttwo.implementation;

import io.github.robertograham.fortniteclienttwo.domain.Account;

import javax.json.JsonObject;
import javax.json.bind.adapter.JsonbAdapter;
import java.util.Objects;

final class DefaultAccount implements Account {

    private final String id;
    private final String displayName;

    private DefaultAccount(JsonObject jsonObject) {
        id = jsonObject.getString("id", null);
        displayName = jsonObject.getString("displayName", null);
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String displayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return "DefaultAccount{" +
                "id='" + id + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (!(object instanceof DefaultAccount))
            return false;
        DefaultAccount defaultAccount = (DefaultAccount) object;
        return Objects.equals(id, defaultAccount.id) &&
                Objects.equals(displayName, defaultAccount.displayName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, displayName);
    }

    enum Adapter implements JsonbAdapter<DefaultAccount, JsonObject> {

        INSTANCE;

        @Override
        public JsonObject adaptToJson(DefaultAccount token) {
            throw new UnsupportedOperationException();
        }

        @Override
        public DefaultAccount adaptFromJson(JsonObject jsonObject) {
            return new DefaultAccount(jsonObject);
        }
    }
}
