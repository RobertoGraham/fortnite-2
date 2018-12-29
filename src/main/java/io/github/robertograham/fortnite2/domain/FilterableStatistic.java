package io.github.robertograham.fortnite2.domain;

import io.github.robertograham.fortnite2.domain.enumeration.PartyType;
import io.github.robertograham.fortnite2.domain.enumeration.Platform;

/**
 * A {@link Statistic} that can be filtered by {@link PartyType} and by {@link Platform}
 *
 * @since 1.0.0
 */
public interface FilterableStatistic extends PlatformFilterableStatistic, PartyTypeFilterableStatistic {

    @Override
    PlatformFilterableStatistic byPartyType(PartyType partyType);

    @Override
    PartyTypeFilterableStatistic byPlatform(Platform platform);
}
