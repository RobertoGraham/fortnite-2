package io.github.robertograham.fortnite2.implementation;

import io.github.robertograham.fortnite2.domain.FilterableStatistic;
import io.github.robertograham.fortnite2.domain.PartyTypeFilterableStatistic;
import io.github.robertograham.fortnite2.domain.PlatformFilterableStatistic;
import io.github.robertograham.fortnite2.domain.Statistic;
import io.github.robertograham.fortnite2.domain.enumeration.PartyType;
import io.github.robertograham.fortnite2.domain.enumeration.Platform;

import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

final class DefaultFilterableStatistic extends StatisticFilterer implements FilterableStatistic {

    private static final Function<Platform, Predicate<RawStatistic>> RAW_STATISTIC_PREDICATE_FACTORY_PLATFORM =
            platform ->
                    rawStatistic -> platform.code().equals(rawStatistic.platform());
    private static final Function<PartyType, Predicate<RawStatistic>> RAW_STATISTIC_PREDICATE_FACTORY_PARTY_TYPE =
            partyType ->
                    rawStatistic -> partyType.code().equals(rawStatistic.partyType());

    DefaultFilterableStatistic(Set<RawStatistic> rawStatistics) {
        super(rawStatistics);
    }

    @Override
    public PartyTypeFilterable byPlatform(Platform platform) {
        return newFilteredStatistic(
                RAW_STATISTIC_PREDICATE_FACTORY_PLATFORM.apply(platform),
                PartyTypeFilterable::new
        );
    }

    @Override
    public PlatformFilterable byPartyType(PartyType partyType) {
        return newFilteredStatistic(
                RAW_STATISTIC_PREDICATE_FACTORY_PARTY_TYPE.apply(partyType),
                PlatformFilterable::new
        );
    }

    @Override
    public String toString() {
        return "DefaultFilterableStatistic{} " + super.toString();
    }

    private static final class PlatformFilterable extends StatisticFilterer implements PlatformFilterableStatistic {

        private PlatformFilterable(Set<RawStatistic> rawStatistics) {
            super(rawStatistics);
        }

        @Override
        public Statistic byPlatform(Platform platform) {
            return newFilteredStatistic(
                    RAW_STATISTIC_PREDICATE_FACTORY_PLATFORM.apply(platform),
                    DefaultStatistic::new
            );
        }

        @Override
        public String toString() {
            return "PlatformFilterable{} " + super.toString();
        }
    }

    private static final class PartyTypeFilterable extends StatisticFilterer implements PartyTypeFilterableStatistic {

        private PartyTypeFilterable(Set<RawStatistic> rawStatistics) {
            super(rawStatistics);
        }

        @Override
        public Statistic byPartyType(PartyType partyType) {
            return newFilteredStatistic(
                    RAW_STATISTIC_PREDICATE_FACTORY_PARTY_TYPE.apply(partyType),
                    DefaultStatistic::new
            );
        }

        @Override
        public String toString() {
            return "PartyTypeFilterable{} " + super.toString();
        }
    }
}
