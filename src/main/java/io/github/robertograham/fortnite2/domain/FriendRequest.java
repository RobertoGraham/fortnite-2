package io.github.robertograham.fortnite2.domain;

import java.time.LocalDateTime;

/**
 * Provides details of a friend request
 *
 * @since 1.3.0
 */
public interface FriendRequest {

    /**
     * @return ID of the account the request was sent to/from
     * @since 1.3.0
     */
    String accountId();

    /**
     * @return usually {@code "ACCEPTED"} if the request has been accepted
     * and {@code "PENDING"} if the request has not been accepted
     * @since 1.3.0
     */
    String status();

    /**
     * @return usually {@code "OUTBOUND"} if the request was sent and
     * {@code "INBOUND"} if the request was received
     * @since 1.3.0
     */
    String direction();

    /**
     * @return the time the request was accepted/sent
     * @since 1.3.0
     */
    LocalDateTime createdAt();

    /**
     * @return whether the account involved has been favourited or not
     * @since 1.3.0
     */
    boolean isFavourite();
}
