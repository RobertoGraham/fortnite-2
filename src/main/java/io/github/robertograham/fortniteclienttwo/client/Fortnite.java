package io.github.robertograham.fortniteclienttwo.client;

import java.util.Objects;

public interface Fortnite {

    abstract class Builder<T extends Builder<T>> {

        private final String epicGamesEmailAddress;
        private final String epicGamesPassword;
        private String epicGamesLauncherToken = "default";
        private String fortniteClientToken = "default";

        Builder(String epicGamesEmailAddress, String epicGamesPassword) {
            this.epicGamesEmailAddress = Objects.requireNonNull(epicGamesEmailAddress, "epicGamesEmailAddress cannot be null");
            this.epicGamesPassword = Objects.requireNonNull(epicGamesPassword, "epicGamesPassword cannot be null");
        }

        public final T setEpicGamesLauncherToken(String epicGamesLauncherToken) {
            this.epicGamesLauncherToken = Objects.requireNonNull(epicGamesLauncherToken, "epicGamesLauncherToken cannot be null");
            return self();
        }

        public final T setFortniteClientToken(String fortniteClientToken) {
            this.fortniteClientToken = Objects.requireNonNull(fortniteClientToken, "fortniteClientToken cannot be null");
            return self();
        }

        public abstract Fortnite build();

        abstract T self();

        final String epicGamesEmailAddress() {
            return epicGamesEmailAddress;
        }

        final String epicGamesPassword() {
            return epicGamesPassword;
        }

        final String epicGamesLauncherToken() {
            return epicGamesLauncherToken;
        }

        final String fortniteClientToken() {
            return fortniteClientToken;
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
            Builder<?> builder = (Builder<?>) object;
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
