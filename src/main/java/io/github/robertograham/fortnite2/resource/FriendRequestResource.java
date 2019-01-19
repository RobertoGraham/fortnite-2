package io.github.robertograham.fortnite2.resource;

import io.github.robertograham.fortnite2.domain.FriendRequest;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * An object from which friend request related API endpoints can be called
 *
 * @since 1.3.0
 */
public interface FriendRequestResource {

    /**
     * @return an {@link Optional} of {@link List} of {@link FriendRequest} that's non-empty
     * if the API response isn't empty
     * @throws IOException if there's an unexpected HTTP status code (less than
     *                     200 or greater than 299) or if there's a problem reading the
     *                     API response
     * @since 1.3.0
     */
    Optional<List<FriendRequest>> findAllBySessionAccountId() throws IOException;

    /**
     * @return an {@link Optional} of {@link List} of {@link FriendRequest} that's non-empty
     * if the API response isn't empty
     * @throws IOException if there's an unexpected HTTP status code (less than
     *                     200 or greater than 299) or if there's a problem reading the
     *                     API response
     * @since 1.3.0
     */
    Optional<List<FriendRequest>> findAllNonPendingBySessionAccountId() throws IOException;
}
