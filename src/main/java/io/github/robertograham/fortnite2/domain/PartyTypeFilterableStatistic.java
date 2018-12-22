package io.github.robertograham.fortnite2.domain;

import io.github.robertograham.fortnite2.domain.enumeration.PartyType;

public interface PartyTypeFilterableStatistic extends Statistic {

    Statistic byPartyType(PartyType partyType);
}
