package io.github.robertograham.fortnite2.resource;

import io.github.robertograham.fortnite2.domain.LeaderBoardEntry;
import io.github.robertograham.fortnite2.domain.enumeration.PartyType;
import io.github.robertograham.fortnite2.domain.enumeration.Platform;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface LeaderBoardResource {

    /**
     * @param platform
     * @param partyType
     * @param maxEntries number of leader board entries to fetch
     * @return list of leader board entries in descending order
     * @throws IllegalArgumentException if maxEntries is less than 0 or greater than 1000
     */
    Optional<List<LeaderBoardEntry>> findHighestWinnersByPlatformAndByPartyTypeForCurrentSeason(Platform platform, PartyType partyType, int maxEntries) throws IOException;
}
