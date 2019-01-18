package io.github.robertograham.fortnite2.implementation;

import io.github.robertograham.fortnite2.domain.Account;

import javax.json.JsonObject;
import javax.json.bind.adapter.JsonbAdapter;
import java.util.Objects;

final class DefaultAccount implements Account {

    private final String accountId;
    private final String displayName;

    private DefaultAccount(final String accountId,
                           final String displayName) {
        this.accountId = accountId;
        this.displayName = displayName;
    }

    @Override
    public String accountId() {
        return accountId;
    }

    @Override
    public String displayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return "DefaultAccount{" +
            "accountId='" + accountId + '\'' +
            ", displayName='" + displayName + '\'' +
            '}';
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object)
            return true;
        if (!(object instanceof DefaultAccount))
            return false;
        final DefaultAccount defaultAccount = (DefaultAccount) object;
        return Objects.equals(accountId, defaultAccount.accountId) &&
            Objects.equals(displayName, defaultAccount.displayName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, displayName);
    }

    enum Adapter implements JsonbAdapter<DefaultAccount, JsonObject> {

        INSTANCE;

        @Override
        public JsonObject adaptToJson(final DefaultAccount defaultAccount) {
            throw new UnsupportedOperationException();
        }

        @Override
        public DefaultAccount adaptFromJson(final JsonObject jsonObject) {
            return new DefaultAccount(
                jsonObject.getString("id", null),
                jsonObject.getString("displayName", null)
            );
        }
    }
}
