package io.github.robertograham.fortnite2.resource;

import io.github.robertograham.fortnite2.domain.LeaderBoardEntry;
import io.github.robertograham.fortnite2.domain.enumeration.PartyType;
import io.github.robertograham.fortnite2.domain.enumeration.Platform;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * An object from which leader board related API endpoints can be called
 *
 * @since 1.0.0
 */
public interface LeaderBoardResource {

    /**
     * @param platform   desired leader board platform
     * @param partyType  desired leader board party type
     * @param maxEntries number of leader board entries to fetch
     * @return an {@link Optional} of {@link List} of {@link LeaderBoardEntry}
     * sorted by wins in descending order that's non-empty if the API response
     * isn't empty
     * @throws IOException              if there's an unexpected HTTP status code (less than
     *                                  200 or greater than 299) or if there's a problem reading the
     *                                  API response
     * @throws NullPointerException     if {@code platform} is {@code null}
     * @throws NullPointerException     if {@code partyType} is {@code null}
     * @throws IllegalArgumentException if {@code maxEntries} is less than 0 or greater than 1000
     * @since 1.0.0
     */
    Optional<List<LeaderBoardEntry>> findHighestWinnersByPlatformAndByPartyTypeForCurrentSeason(final Platform platform,
                                                                                                final PartyType partyType,
                                                                                                final int maxEntries) throws IOException;
}
