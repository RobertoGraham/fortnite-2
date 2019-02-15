package io.github.robertograham.fortnite2.implementation;

import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.bind.adapter.JsonbAdapter;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

final class StatisticsV2 {

    private final long startTime;
    private final long endTime;
    private final String accountId;
    private final Set<RawStatisticV2> rawStatistics;

    private StatisticsV2(final long startTime,
                         final long endTime,
                         final String accountId,
                         final Set<RawStatisticV2> rawStatistics) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.accountId = accountId;
        this.rawStatistics = rawStatistics;
    }

    long startTime() {
        return startTime;
    }

    long endTime() {
        return endTime;
    }

    String accountId() {
        return accountId;
    }

    Set<RawStatisticV2> rawStatistics() {
        return rawStatistics;
    }

    @Override
    public String toString() {
        return "StatisticsV2{" +
            "startTime=" + startTime +
            ", endTime=" + endTime +
            ", accountId='" + accountId + '\'' +
            ", rawStatistics=" + rawStatistics +
            '}';
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object)
            return true;
        if (!(object instanceof StatisticsV2))
            return false;
        final var statisticsV2 = (StatisticsV2) object;
        return startTime == statisticsV2.startTime &&
            endTime == statisticsV2.endTime &&
            accountId.equals(statisticsV2.accountId) &&
            rawStatistics.equals(statisticsV2.rawStatistics);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime, accountId, rawStatistics);
    }

    enum Adapter implements JsonbAdapter<StatisticsV2, JsonObject> {

        INSTANCE;

        @Override
        public JsonObject adaptToJson(final StatisticsV2 statisticsV2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public StatisticsV2 adaptFromJson(final JsonObject jsonObject) {
            return new StatisticsV2(
                jsonObject.getJsonNumber("startTime")
                    .longValueExact(),
                jsonObject.getJsonNumber("endTime")
                    .longValueExact(),
                jsonObject.getString("accountId"),
                jsonObject.getJsonObject("stats")
                    .entrySet()
                    .stream()
                    .filter((final var stringJsonValueEntry) -> stringJsonValueEntry.getValue() instanceof JsonNumber)
                    .map((final var stringJsonValueEntry) -> new RawStatisticV2(stringJsonValueEntry.getKey(), ((JsonNumber) stringJsonValueEntry.getValue()).longValueExact()))
                    .collect(Collectors.toSet())
            );
        }
    }
}
