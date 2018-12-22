package io.github.robertograham.fortniteclienttwo.domain;

import io.github.robertograham.fortniteclienttwo.domain.enumeration.PartyType;
import io.github.robertograham.fortniteclienttwo.domain.enumeration.Platform;

public interface FilterableStatistic extends PlatformFilterableStatistic, PartyTypeFilterableStatistic {

    @Override
    PlatformFilterableStatistic byPartyType(PartyType partyType);

    @Override
    PartyTypeFilterableStatistic byPlatform(Platform platform);
}
