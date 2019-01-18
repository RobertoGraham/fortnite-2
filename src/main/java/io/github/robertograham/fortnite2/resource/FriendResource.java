package io.github.robertograham.fortnite2.resource;

import java.io.IOException;
import java.util.Optional;

/**
 * An object from which friend related API endpoints can be called
 *
 * @since 1.3.0
 */
public interface FriendResource {

    /**
     * @return an {@link Optional} of {@link String} that's non-empty
     * is the API response isn't empty
     * @throws IOException if there's an unexpected HTTP status code (less than
     *                     200 or greater than 299) or if there's a problem reading the
     *                     API response
     * @since 1.3.0
     */
    Optional<String> findAllBySessionAccountId() throws IOException;
}
