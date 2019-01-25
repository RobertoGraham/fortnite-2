package io.github.robertograham.fortnite2.implementation;

import io.github.robertograham.fortnite2.domain.Statistic;

import java.time.LocalDateTime;
import java.util.Objects;

abstract class ForwardingStatistic implements Statistic {

    private final Statistic statistic;

    ForwardingStatistic(final Statistic statistic) {
        this.statistic = statistic;
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
    public final LocalDateTime timeLastModified() {
        return statistic.timeLastModified();
    }

    @Override
    public String toString() {
        return "ForwardingStatistic{" +
            "statistic=" + statistic +
            '}';
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object)
            return true;
        if (!(object instanceof ForwardingStatistic))
            return false;
        final var forwardingStatistic = (ForwardingStatistic) object;
        return statistic.equals(forwardingStatistic.statistic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statistic);
    }
}