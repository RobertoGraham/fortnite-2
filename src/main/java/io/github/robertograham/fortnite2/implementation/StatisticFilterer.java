package io.github.robertograham.fortnite2.implementation;

import io.github.robertograham.fortnite2.domain.Statistic;

import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

abstract class StatisticFilterer extends ForwardingStatistic {

    private final Set<RawStatistic> rawStatistics;

    StatisticFilterer(final Set<RawStatistic> rawStatistics) {
        super(new DefaultStatistic(rawStatistics));
        this.rawStatistics = rawStatistics;
    }

    final <T extends Statistic> T newFilteredStatistic(final Predicate<RawStatistic> rawStatisticPredicate,
                                                       final Function<Set<RawStatistic>, T> statisticFactory) {
        return statisticFactory.apply(
            rawStatistics.stream()
                .filter(rawStatisticPredicate)
                .collect(Collectors.toSet())
        );
    }

    @Override
    public String toString() {
        return "StatisticFilterer{" +
            "rawStatistics=" + rawStatistics +
            "} " + super.toString();
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object)
            return true;
        if (!(object instanceof StatisticFilterer))
            return false;
        if (!super.equals(object))
            return false;
        final StatisticFilterer statisticFilterer = (StatisticFilterer) object;
        return rawStatistics.equals(statisticFilterer.rawStatistics);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), rawStatistics);
    }
}
