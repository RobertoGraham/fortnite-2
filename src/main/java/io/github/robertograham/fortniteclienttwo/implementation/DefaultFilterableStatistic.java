package io.github.robertograham.fortniteclienttwo.implementation;

import io.github.robertograham.fortniteclienttwo.domain.FilterableStatistic;
import io.github.robertograham.fortniteclienttwo.domain.Statistic;
import io.github.robertograham.fortniteclienttwo.domain.enumeration.PartyType;
import io.github.robertograham.fortniteclienttwo.domain.enumeration.Platform;

import java.util.Set;
import java.util.stream.Collectors;

final class DefaultFilterableStatistic extends ForwardingStatistic implements FilterableStatistic {

    DefaultFilterableStatistic(Set<RawStatistic> rawStatistics) {
        super(rawStatistics);
    }

    @Override
    public PartyTypeFilterableStatistic byPlatform(Platform platform) {
        return new PartyTypeFilterableStatistic(
                rawStatistics().stream()
                        .filter(rawStatistic -> platform.code().equals(rawStatistic.platform()))
                        .collect(Collectors.toSet())
        );
    }

    @Override
    public PlatformFilterableStatistic byPartyType(PartyType partyType) {
        return new PlatformFilterableStatistic(
                rawStatistics().stream()
                        .filter(rawStatistic -> partyType.code().equals(rawStatistic.partyType()))
                        .collect(Collectors.toSet())
        );
    }

    @Override
    public String toString() {
        return "DefaultFilterableStatistic{} " + super.toString();
    }

    private static final class PlatformFilterableStatistic extends ForwardingStatistic implements io.github.robertograham.fortniteclienttwo.domain.PlatformFilterableStatistic {

        private PlatformFilterableStatistic(Set<RawStatistic> rawStatistics) {
            super(rawStatistics);
        }

        @Override
        public Statistic byPlatform(Platform platform) {
            return matches() > 0L ?
                    new DefaultStatistic(
                            rawStatistics().stream()
                                    .filter(rawStatistic -> platform.code().equals(rawStatistic.platform()))
                                    .collect(Collectors.toSet())
                    )
                    : EmptyStatistic.INSTANCE;
        }

        @Override
        public String toString() {
            return "PlatformFilterableStatistic{} " + super.toString();
        }
    }

    private static final class PartyTypeFilterableStatistic extends ForwardingStatistic implements io.github.robertograham.fortniteclienttwo.domain.PartyTypeFilterableStatistic {

        private PartyTypeFilterableStatistic(Set<RawStatistic> rawStatistics) {
            super(rawStatistics);
        }

        @Override
        public Statistic byPartyType(PartyType partyType) {
            return matches() > 0L ?
                    new DefaultStatistic(
                            rawStatistics().stream()
                                    .filter(rawStatistic -> partyType.code().equals(rawStatistic.partyType()))
                                    .collect(Collectors.toSet())
                    )
                    : EmptyStatistic.INSTANCE;
        }

        @Override
        public String toString() {
            return "PartyTypeFilterableStatistic{} " + super.toString();
        }
    }
}
