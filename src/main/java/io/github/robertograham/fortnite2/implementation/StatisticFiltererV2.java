package io.github.robertograham.fortnite2.implementation;

import io.github.robertograham.fortnite2.domain.Statistic;

import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

abstract class StatisticFiltererV2 extends ForwardingStatistic {

    private final Set<RawStatisticV2> rawStatistics;

    StatisticFiltererV2(final Set<RawStatisticV2> rawStatistics) {
        super(new DefaultStatisticV2(rawStatistics));
        this.rawStatistics = rawStatistics;
    }

    final <T extends Statistic> T newFilteredStatistic(final Predicate<RawStatisticV2> rawStatisticPredicate,
                                                       final Function<Set<RawStatisticV2>, T> statisticFactory) {
        return statisticFactory.apply(rawStatistics.stream()
            .filter(rawStatisticPredicate)
            .collect(Collectors.toSet()));
    }

    @Override
    public String toString() {
        return "StatisticFiltererV2{" +
            "rawStatistics=" + rawStatistics +
            "} " + super.toString();
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object)
            return true;
        if (!(object instanceof StatisticFiltererV2))
            return false;
        if (!super.equals(object))
            return false;
        final var statisticFiltererV2 = (StatisticFiltererV2) object;
        return rawStatistics.equals(statisticFiltererV2.rawStatistics);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), rawStatistics);
    }
}
