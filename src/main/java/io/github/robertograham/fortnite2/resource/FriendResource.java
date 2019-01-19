package io.github.robertograham.fortnite2.resource;

import io.github.robertograham.fortnite2.domain.Account;
import io.github.robertograham.fortnite2.domain.FriendRequest;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * An object from which friend related API endpoints can be called
 *
 * @since 1.3.0
 */
public interface FriendResource {

    /**
     * @return an {@link Optional} of {@link List} of {@link FriendRequest} that's non-empty
     * if the API response isn't empty
     * @throws IOException if there's an unexpected HTTP status code (less than
     *                     200 or greater than 299) or if there's a problem reading the
     *                     API response
     * @since 1.3.0
     */
    Optional<List<FriendRequest>> findAllRequestsBySessionAccountId() throws IOException;

    /**
     * @return an {@link Optional} of {@link List} of {@link FriendRequest} that's non-empty
     * if the API response isn't empty
     * @throws IOException if there's an unexpected HTTP status code (less than
     *                     200 or greater than 299) or if there's a problem reading the
     *                     API response
     * @since 1.3.0
     */
    Optional<List<FriendRequest>> findAllNonPendingRequestsBySessionAccountId() throws IOException;

    /**
     * Deletes friend or friend request
     *
     * @param accountId ID of the account to unfriend or the friend request to delete's account ID
     * @throws IOException if there's an unexpected HTTP status code (less than
     *                     200 or greater than 299) or if there's a problem reading the
     *                     API response
     * @since 1.3.0
     */
    void deleteOneByAccountId(final String accountId) throws IOException;

    /**
     * Deletes friend or friend request
     *
     * @param account the account to unfriend or the friend request to delete's account
     * @throws IOException if there's an unexpected HTTP status code (less than
     *                     200 or greater than 299) or if there's a problem reading the
     *                     API response
     * @since 1.3.0
     */
    default void deleteOneByAccount(final Account account) throws IOException {
        Objects.requireNonNull(account, "account cannot be null");
        deleteOneByAccountId(account.accountId());
    }

    /**
     * Adds friend or accept friend request
     *
     * @param accountId the friend request to accept's account ID or the account ID to send a
     *                  friend request to
     * @throws IOException if there's an unexpected HTTP status code (less than
     *                     200 or greater than 299) or if there's a problem reading the
     *                     API response
     * @since 1.3.0
     */
    void addOneByAccountId(final String accountId) throws IOException;

    /**
     * Adds friend or accept friend request
     *
     * @param account the friend request to accept's account or the account to send a
     *                friend request to
     * @throws IOException if there's an unexpected HTTP status code (less than
     *                     200 or greater than 299) or if there's a problem reading the
     *                     API response
     * @since 1.3.0
     */
    default void addOneByAccount(final Account account) throws IOException {
        Objects.requireNonNull(account, "account cannot be null");
        addOneByAccountId(account.accountId());
    }
}
