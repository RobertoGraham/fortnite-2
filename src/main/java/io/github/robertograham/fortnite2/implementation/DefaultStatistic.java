package io.github.robertograham.fortnite2.implementation;

import io.github.robertograham.fortnite2.domain.Statistic;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


final class DefaultStatistic implements Statistic {

    private final long wins;
    private final long matches;
    private final long kills;
    private final long score;
    private final long timesPlacedTop10;
    private final long timesPlacedTop25;
    private final long timesPlacedTop5;
    private final long timesPlacedTop12;
    private final long timesPlacedTop3;
    private final long timesPlacedTop6;
    private final LocalDateTime timeLastModified;

    DefaultStatistic(final Set<RawStatistic> rawStatistics) {
        final Map<String, Long> summedValuesGroupedByStatType =
            rawStatistics.stream()
                .filter((final var rawStatistic) -> !"lastmodified".equals(rawStatistic.type()))
                .collect(Collectors.groupingBy(
                    RawStatistic::type,
                    Collectors.summingLong(RawStatistic::value)
                ));
        wins = summedValuesGroupedByStatType.getOrDefault("placetop1", 0L);
        matches = summedValuesGroupedByStatType.getOrDefault("matchesplayed", 0L);
        kills = summedValuesGroupedByStatType.getOrDefault("kills", 0L);
        score = summedValuesGroupedByStatType.getOrDefault("score", 0L);
        timesPlacedTop10 = summedValuesGroupedByStatType.getOrDefault("placetop10", 0L);
        timesPlacedTop25 = summedValuesGroupedByStatType.getOrDefault("placetop25", 0L);
        timesPlacedTop5 = summedValuesGroupedByStatType.getOrDefault("placetop5", 0L);
        timesPlacedTop12 = summedValuesGroupedByStatType.getOrDefault("placetop12", 0L);
        timesPlacedTop3 = summedValuesGroupedByStatType.getOrDefault("placetop3", 0L);
        timesPlacedTop6 = summedValuesGroupedByStatType.getOrDefault("placetop6", 0L);
        timeLastModified = rawStatistics.stream()
            .filter(rawStatistic -> "lastmodified".equals(rawStatistic.type()))
            .max(Comparator.comparingLong(RawStatistic::value))
            .map((final var rawStatistic) ->
                LocalDateTime.ofInstant(
                    Instant.ofEpochSecond(rawStatistic.value()),
                    ZoneOffset.UTC
                )
            )
            .orElse(LocalDateTime.MIN);
    }

    @Override
    public long wins() {
        return wins;
    }

    @Override
    public long matches() {
        return matches;
    }

    @Override
    public long kills() {
        return kills;
    }

    @Override
    public long score() {
        return score;
    }

    @Override
    public long timesPlacedTop10() {
        return timesPlacedTop10;
    }

    @Override
    public long timesPlacedTop25() {
        return timesPlacedTop25;
    }

    @Override
    public long timesPlacedTop5() {
        return timesPlacedTop5;
    }

    @Override
    public long timesPlacedTop12() {
        return timesPlacedTop12;
    }

    @Override
    public long timesPlacedTop3() {
        return timesPlacedTop3;
    }

    @Override
    public long timesPlacedTop6() {
        return timesPlacedTop6;
    }

    @Override
    public LocalDateTime timeLastModified() {
        return timeLastModified;
    }

    @Override
    public String toString() {
        return "DefaultStatistic{" +
            "wins=" + wins +
            ", matches=" + matches +
            ", kills=" + kills +
            ", score=" + score +
            ", timesPlacedTop10=" + timesPlacedTop10 +
            ", timesPlacedTop25=" + timesPlacedTop25 +
            ", timesPlacedTop5=" + timesPlacedTop5 +
            ", timesPlacedTop12=" + timesPlacedTop12 +
            ", timesPlacedTop3=" + timesPlacedTop3 +
            ", timesPlacedTop6=" + timesPlacedTop6 +
            ", timeLastModified=" + timeLastModified +
            '}';
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object)
            return true;
        if (!(object instanceof DefaultStatistic))
            return false;
        final var defaultStatistic = (DefaultStatistic) object;
        return wins == defaultStatistic.wins &&
            matches == defaultStatistic.matches &&
            kills == defaultStatistic.kills &&
            score == defaultStatistic.score &&
            timesPlacedTop10 == defaultStatistic.timesPlacedTop10 &&
            timesPlacedTop25 == defaultStatistic.timesPlacedTop25 &&
            timesPlacedTop5 == defaultStatistic.timesPlacedTop5 &&
            timesPlacedTop12 == defaultStatistic.timesPlacedTop12 &&
            timesPlacedTop3 == defaultStatistic.timesPlacedTop3 &&
            timesPlacedTop6 == defaultStatistic.timesPlacedTop6 &&
            timeLastModified.equals(defaultStatistic.timeLastModified);
    }

    @Override
    public int hashCode() {
        return Objects.hash(wins, matches, kills, score, timesPlacedTop10, timesPlacedTop25, timesPlacedTop5, timesPlacedTop12, timesPlacedTop3, timesPlacedTop6, timeLastModified);
    }
}