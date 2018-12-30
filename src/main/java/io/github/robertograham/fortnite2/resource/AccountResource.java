package io.github.robertograham.fortnite2.resource;

import io.github.robertograham.fortnite2.domain.Account;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

/**
 * An object from which account related API endpoints can be called
 *
 * @since 1.0.0
 */
public interface AccountResource {

    /**
     * @param displayName username of the Epic Games account to be retrieved
     * @return an {@link Optional} of {@link Account} that's non-empty if an
     * account matching the supplied username was found
     * @throws IOException          if there's an unexpected HTTP status code (less than
     *                              200 or greater than 299) or if there's a problem reading the
     *                              API response
     * @throws NullPointerException if {@code displayName} is {@code null}
     * @since 1.0.0
     */
    Optional<Account> findOneByDisplayName(String displayName) throws IOException;

    /**
     * @return an {@link Optional} of {@link Account} that's non-empty if an
     * account matching the current session's account ID was found
     * @throws IOException if there's an unexpected HTTP status code (less than
     *                     200 or greater than 299) or if there's a problem reading the
     *                     API response
     * @since 1.2.0
     */
    default Optional<Account> findOneBySessionAccountId() throws IOException {
        return Optional.empty();
    }

    /**
     * @param accountIds IDs of the Epic Games accounts to be retrieved
     * @return an {@link Optional} of {@link Set} of {@link Account} that's
     * non-empty if any accounts matching the supplied IDs were found
     * @throws IOException          if there's an unexpected HTTP status code (less than
     *                              200 or greater than 299) or if there's a problem reading the
     *                              API response
     * @throws NullPointerException if the {@code accountIds} is {@code null}
     * @throws NullPointerException if any of the supplied IDs are {@code null}
     * @since 1.0.0
     */
    Optional<Set<Account>> findAllByAccountIds(String... accountIds) throws IOException;
}
