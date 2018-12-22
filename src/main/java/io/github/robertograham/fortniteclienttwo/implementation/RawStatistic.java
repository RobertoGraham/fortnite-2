package io.github.robertograham.fortniteclienttwo.implementation;

import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.bind.adapter.JsonbAdapter;
import java.util.Objects;
import java.util.Optional;

final class RawStatistic {

    private final String statType;
    private final String platform;
    private final String partyType;
    private final long value;

    private RawStatistic(JsonObject jsonObject) {
        final String[] nameParts = jsonObject.getString("name", "____").split("_");
        statType = nameParts[1];
        platform = nameParts[2];
        partyType = nameParts[4];
        value = Optional.ofNullable(jsonObject.getJsonNumber("value"))
                .map(JsonNumber::longValueExact)
                .orElse(0L);
    }

    String statType() {
        return statType;
    }

    String platform() {
        return platform;
    }

    String partyType() {
        return partyType;
    }

    long value() {
        return value;
    }

    @Override
    public String toString() {
        return "RawStatistic{" +
                "statType='" + statType + '\'' +
                ", platform='" + platform + '\'' +
                ", partyType='" + partyType + '\'' +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (!(object instanceof RawStatistic))
            return false;
        RawStatistic rawStatistic = (RawStatistic) object;
        return value == rawStatistic.value &&
                statType.equals(rawStatistic.statType) &&
                platform.equals(rawStatistic.platform) &&
                partyType.equals(rawStatistic.partyType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statType, platform, partyType, value);
    }

    enum Adapter implements JsonbAdapter<RawStatistic, JsonObject> {

        INSTANCE;

        @Override
        public JsonObject adaptToJson(RawStatistic rawStatistic) {
            throw new UnsupportedOperationException();
        }

        @Override
        public RawStatistic adaptFromJson(JsonObject jsonObject) {
            return new RawStatistic(jsonObject);
        }
    }
}
