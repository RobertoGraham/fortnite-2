package io.github.robertograham.fortnite2.domain;

import io.github.robertograham.fortnite2.domain.enumeration.PartyType;
import io.github.robertograham.fortnite2.domain.enumeration.Platform;

public interface FilterableStatistic extends PlatformFilterableStatistic, PartyTypeFilterableStatistic {

    @Override
    PlatformFilterableStatistic byPartyType(PartyType partyType);

    @Override
    PartyTypeFilterableStatistic byPlatform(Platform platform);
}
