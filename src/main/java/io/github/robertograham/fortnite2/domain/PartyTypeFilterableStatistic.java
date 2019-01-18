package io.github.robertograham.fortnite2.domain;

import io.github.robertograham.fortnite2.domain.enumeration.PartyType;

/**
 * A {@link Statistic} that can be filtered by {@link PartyType}
 *
 * @since 1.0.0
 */
public interface PartyTypeFilterableStatistic extends Statistic {

    /**
     * @param partyType the party type to filter this statistic by
     * @return a {@link Statistic} with values scoped to {@code partyType}
     * @since 1.0.0
     */
    Statistic byPartyType(final PartyType partyType);
}
