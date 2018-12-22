package io.github.robertograham.fortnite2.implementation;

import javax.json.JsonObject;
import javax.json.bind.adapter.JsonbAdapter;
import java.util.Objects;

final class RawStatistic {

    private final String type;
    private final String platform;
    private final String partyType;
    private final long value;

    private RawStatistic(String type,
                         String platform,
                         String partyType,
                         long value) {
        this.type = type;
        this.platform = platform;
        this.partyType = partyType;
        this.value = value;
    }

    String type() {
        return type;
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
                "type='" + type + '\'' +
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
                type.equals(rawStatistic.type) &&
                platform.equals(rawStatistic.platform) &&
                partyType.equals(rawStatistic.partyType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, platform, partyType, value);
    }

    enum Adapter implements JsonbAdapter<RawStatistic, JsonObject> {

        INSTANCE;

        @Override
        public JsonObject adaptToJson(RawStatistic rawStatistic) {
            throw new UnsupportedOperationException();
        }

        @Override
        public RawStatistic adaptFromJson(JsonObject jsonObject) {
            final String[] nameParts =
                    jsonObject.getString("name")
                            .split("_");
            return new RawStatistic(
                    nameParts[1],
                    nameParts[2],
                    nameParts[4],
                    jsonObject.getJsonNumber("value").longValueExact()
            );
        }
    }
}
