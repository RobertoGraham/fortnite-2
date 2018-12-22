package io.github.robertograham.fortnite2.implementation;

import io.github.robertograham.fortnite2.domain.LeaderBoardEntry;

import java.util.Objects;

final class DefaultLeaderBoardEntry implements LeaderBoardEntry {

    private final String accountId;
    private final long value;

    DefaultLeaderBoardEntry(String accountId,
                            long value) {
        this.accountId = accountId;
        this.value = value;
    }

    @Override
    public String accountId() {
        return accountId;
    }

    @Override
    public long value() {
        return value;
    }

    @Override
    public String toString() {
        return "DefaultLeaderBoardEntry{" +
                "accountId='" + accountId + '\'' +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (!(object instanceof DefaultLeaderBoardEntry))
            return false;
        DefaultLeaderBoardEntry defaultLeaderBoardEntry = (DefaultLeaderBoardEntry) object;
        return value == defaultLeaderBoardEntry.value &&
                accountId.equals(defaultLeaderBoardEntry.accountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, value);
    }
}
