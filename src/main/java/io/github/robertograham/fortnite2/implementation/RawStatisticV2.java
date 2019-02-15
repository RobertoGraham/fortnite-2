package io.github.robertograham.fortnite2.implementation;

import java.util.Objects;

final class RawStatisticV2 {

    private final String type;
    private final String inputType;
    private final String mode;
    private final long value;

    RawStatisticV2(final String playlist, final long value) {
        this.value = value;
        final var playlistComponentsStringArray = playlist.split("_", 6);
        type = playlistComponentsStringArray[1];
        inputType = playlistComponentsStringArray[2];
        mode = playlistComponentsStringArray[5];
    }

    public String type() {
        return type;
    }

    public String inputType() {
        return inputType;
    }

    public String mode() {
        return mode;
    }

    public long value() {
        return value;
    }

    @Override
    public String toString() {
        return "RawStatisticV2{" +
            "type='" + type + '\'' +
            ", inputType='" + inputType + '\'' +
            ", mode='" + mode + '\'' +
            ", value=" + value +
            '}';
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object)
            return true;
        if (!(object instanceof RawStatisticV2))
            return false;
        final var rawStatisticV2 = (RawStatisticV2) object;
        return value == rawStatisticV2.value &&
            type.equals(rawStatisticV2.type) &&
            inputType.equals(rawStatisticV2.inputType) &&
            mode.equals(rawStatisticV2.mode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, inputType, mode, value);
    }
}
