package io.github.robertograham.fortniteclienttwo.client;

import java.util.Objects;

public final class DefaultFortnite implements Fortnite {

    private final String epicGamesEmailAddress;
    private final String epicGamesPassword;
    private final String epicGamesLauncherToken;
    private final String fortniteClientToken;

    private DefaultFortnite(Builder builder) {
        epicGamesEmailAddress = builder.epicGamesEmailAddress;
        epicGamesPassword = builder.epicGamesPassword;
        epicGamesLauncherToken = builder.epicGamesLauncherToken;
        fortniteClientToken = builder.fortniteClientToken;
    }

    @Override
    public String toString() {
        return "DefaultFortnite{" +
                "epicGamesEmailAddress='" + epicGamesEmailAddress + '\'' +
                ", epicGamesPassword='" + epicGamesPassword + '\'' +
                ", epicGamesLauncherToken='" + epicGamesLauncherToken + '\'' +
                ", fortniteClientToken='" + fortniteClientToken + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (!(object instanceof DefaultFortnite))
            return false;
        DefaultFortnite defaultFortnite = (DefaultFortnite) object;
        return epicGamesEmailAddress.equals(defaultFortnite.epicGamesEmailAddress) &&
                epicGamesPassword.equals(defaultFortnite.epicGamesPassword) &&
                epicGamesLauncherToken.equals(defaultFortnite.epicGamesLauncherToken) &&
                fortniteClientToken.equals(defaultFortnite.fortniteClientToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(epicGamesEmailAddress, epicGamesPassword, epicGamesLauncherToken, fortniteClientToken);
    }

    public static final class Builder {

        private final String epicGamesEmailAddress;
        private final String epicGamesPassword;
        private String epicGamesLauncherToken = "default";
        private String fortniteClientToken = "default";

        private Builder(String epicGamesEmailAddress, String epicGamesPassword) {
            this.epicGamesEmailAddress = epicGamesEmailAddress;
            this.epicGamesPassword = epicGamesPassword;
        }

        public static Builder newInstance(String epicGamesEmailAddress, String epicGamesPassword) {
            Objects.requireNonNull(epicGamesEmailAddress, "epicGamesEmailAddress cannot be null");
            Objects.requireNonNull(epicGamesPassword, "epicGamesPassword cannot be null");
            return new Builder(epicGamesEmailAddress, epicGamesPassword);
        }

        public final Builder setEpicGamesLauncherToken(String epicGamesLauncherToken) {
            this.epicGamesLauncherToken = Objects.requireNonNull(epicGamesLauncherToken, "epicGamesLauncherToken cannot be null");
            return this;
        }

        public final Builder setFortniteClientToken(String fortniteClientToken) {
            this.fortniteClientToken = Objects.requireNonNull(fortniteClientToken, "fortniteClientToken cannot be null");
            return this;
        }

        public DefaultFortnite build() {
            return new DefaultFortnite(this);
        }

        @Override
        public String toString() {
            return "Builder{" +
                    "epicGamesEmailAddress='" + epicGamesEmailAddress + '\'' +
                    ", epicGamesPassword='" + epicGamesPassword + '\'' +
                    ", epicGamesLauncherToken='" + epicGamesLauncherToken + '\'' +
                    ", fortniteClientToken='" + fortniteClientToken + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object object) {
            if (this == object)
                return true;
            if (!(object instanceof Builder))
                return false;
            Builder builder = (Builder) object;
            return epicGamesEmailAddress.equals(builder.epicGamesEmailAddress) &&
                    epicGamesPassword.equals(builder.epicGamesPassword) &&
                    epicGamesLauncherToken.equals(builder.epicGamesLauncherToken) &&
                    fortniteClientToken.equals(builder.fortniteClientToken);
        }

        @Override
        public int hashCode() {
            return Objects.hash(epicGamesEmailAddress, epicGamesPassword, epicGamesLauncherToken, fortniteClientToken);
        }
    }
}
