package io.github.robertograham.fortnite2.implementation;

import io.github.robertograham.fortnite2.domain.Statistic;

import java.util.Objects;
import java.util.Set;

abstract class ForwardingStatistic implements Statistic {

    private final Set<RawStatistic> rawStatistics;
    private final Statistic statistic;

    ForwardingStatistic(Set<RawStatistic> rawStatistics) {
        this.rawStatistics = rawStatistics;
        statistic = rawStatistics.isEmpty() ?
                EmptyStatistic.INSTANCE
                : new DefaultStatistic(this.rawStatistics);
    }

    final Set<RawStatistic> rawStatistics() {
        return rawStatistics;
    }

    @Override
    public final long wins() {
        return statistic.wins();
    }

    @Override
    public final long matches() {
        return statistic.matches();
    }

    @Override
    public final long kills() {
        return statistic.kills();
    }

    @Override
    public final long score() {
        return statistic.score();
    }

    @Override
    public final long timesPlacedTop10() {
        return statistic.timesPlacedTop10();
    }

    @Override
    public final long timesPlacedTop25() {
        return statistic.timesPlacedTop25();
    }

    @Override
    public final long timesPlacedTop5() {
        return statistic.timesPlacedTop5();
    }

    @Override
    public final long timesPlacedTop12() {
        return statistic.timesPlacedTop12();
    }

    @Override
    public final long timesPlacedTop3() {
        return statistic.timesPlacedTop3();
    }

    @Override
    public final long timesPlacedTop6() {
        return statistic.timesPlacedTop6();
    }

    @Override
    public String toString() {
        return "ForwardingStatistic{" +
                "rawStatistics=" + rawStatistics +
                ", statistic=" + statistic +
                '}';
    }

    @Override
    public final boolean equals(Object object) {
        if (this == object)
            return true;
        if (!(object instanceof ForwardingStatistic))
            return false;
        ForwardingStatistic forwardingStatistic = (ForwardingStatistic) object;
        return rawStatistics.equals(forwardingStatistic.rawStatistics) &&
                statistic.equals(forwardingStatistic.statistic);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(rawStatistics, statistic);
    }
}