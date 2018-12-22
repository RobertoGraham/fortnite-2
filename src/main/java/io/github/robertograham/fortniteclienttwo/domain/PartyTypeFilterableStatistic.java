package io.github.robertograham.fortniteclienttwo.domain;

import io.github.robertograham.fortniteclienttwo.domain.enumeration.PartyType;

public interface PartyTypeFilterableStatistic extends Statistic {

    Statistic byPartyType(PartyType partyType);
}
