package io.github.robertograham.fortnite2.implementation;

import javax.json.JsonObject;
import javax.json.bind.adapter.JsonbAdapter;
import java.util.Objects;

final class Eula {

    private final long version;

    private Eula(final long version) {
        this.version = version;
    }

    long version() {
        return version;
    }

    @Override
    public String toString() {
        return "Eula{" +
            "version=" + version +
            '}';
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object)
            return true;
        if (!(object instanceof Eula))
            return false;
        final var eula = (Eula) object;
        return version == eula.version;
    }

    @Override
    public int hashCode() {
        return Objects.hash(version);
    }

    enum Adapter implements JsonbAdapter<Eula, JsonObject> {

        INSTANCE;

        @Override
        public JsonObject adaptToJson(final Eula eula) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Eula adaptFromJson(final JsonObject jsonObject) {
            return new Eula(jsonObject.getJsonNumber("version")
                .longValueExact());
        }
    }
}
