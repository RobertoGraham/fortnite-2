package io.github.robertograham.fortnite2.implementation;

import io.github.robertograham.fortnite2.domain.FriendRequest;

import javax.json.JsonObject;
import javax.json.bind.adapter.JsonbAdapter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

final class DefaultFriendRequest implements FriendRequest {

    private final String accountId;
    private final String status;
    private final String direction;
    private final LocalDateTime createdAt;
    private final boolean favourite;

    private DefaultFriendRequest(final String accountId,
                                 final String status,
                                 final String direction,
                                 final LocalDateTime createdAt,
                                 final boolean favourite) {
        this.accountId = accountId;
        this.status = status;
        this.direction = direction;
        this.createdAt = createdAt;
        this.favourite = favourite;
    }

    @Override
    public String accountId() {
        return accountId;
    }

    @Override
    public String status() {
        return status;
    }

    @Override
    public String direction() {
        return direction;
    }

    @Override
    public LocalDateTime createdAt() {
        return createdAt;
    }

    @Override
    public boolean isFavourite() {
        return favourite;
    }

    @Override
    public String toString() {
        return "DefaultFriendRequest{" +
            "accountId='" + accountId + '\'' +
            ", status='" + status + '\'' +
            ", direction='" + direction + '\'' +
            ", createdAt=" + createdAt +
            ", favourite=" + favourite +
            '}';
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object)
            return true;
        if (!(object instanceof DefaultFriendRequest))
            return false;
        final var defaultFriendRequest = (DefaultFriendRequest) object;
        return favourite == defaultFriendRequest.favourite &&
            accountId.equals(defaultFriendRequest.accountId) &&
            status.equals(defaultFriendRequest.status) &&
            direction.equals(defaultFriendRequest.direction) &&
            createdAt.equals(defaultFriendRequest.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, status, direction, createdAt, favourite);
    }

    enum Adapter implements JsonbAdapter<DefaultFriendRequest, JsonObject> {

        INSTANCE;

        @Override
        public JsonObject adaptToJson(final DefaultFriendRequest friendRequest) {
            throw new UnsupportedOperationException();
        }

        @Override
        public DefaultFriendRequest adaptFromJson(final JsonObject jsonObject) {
            return new DefaultFriendRequest(
                jsonObject.getString("accountId"),
                jsonObject.getString("status"),
                jsonObject.getString("direction"),
                LocalDateTime.ofInstant(
                    Instant.parse(jsonObject.getString("created")),
                    ZoneOffset.UTC
                ),
                jsonObject.getBoolean("favorite")
            );
        }
    }
}
