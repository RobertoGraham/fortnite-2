package io.github.robertograham.fortniteclienttwo.client;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.Objects;

public final class ApacheFortnite implements Fortnite {

    private final String epicGamesEmailAddress;
    private final String epicGamesPassword;
    private final String epicGamesLauncherToken;
    private final String fortniteClientToken;
    private final HttpClient httpClient;

    private ApacheFortnite(Builder builder) {
        epicGamesEmailAddress = builder.epicGamesEmailAddress();
        epicGamesPassword = builder.epicGamesPassword();
        epicGamesLauncherToken = builder.epicGamesLauncherToken();
        fortniteClientToken = builder.fortniteClientToken();
        httpClient = builder.httpClient;
    }

    @Override
    public String toString() {
        return "ApacheFortnite{" +
                "epicGamesEmailAddress='" + epicGamesEmailAddress + '\'' +
                ", epicGamesPassword='" + epicGamesPassword + '\'' +
                ", epicGamesLauncherToken='" + epicGamesLauncherToken + '\'' +
                ", fortniteClientToken='" + fortniteClientToken + '\'' +
                ", httpClient=" + httpClient +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (!(object instanceof ApacheFortnite))
            return false;
        ApacheFortnite apacheFortnite = (ApacheFortnite) object;
        return epicGamesEmailAddress.equals(apacheFortnite.epicGamesEmailAddress) &&
                epicGamesPassword.equals(apacheFortnite.epicGamesPassword) &&
                epicGamesLauncherToken.equals(apacheFortnite.epicGamesLauncherToken) &&
                fortniteClientToken.equals(apacheFortnite.fortniteClientToken) &&
                httpClient.equals(apacheFortnite.httpClient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(epicGamesEmailAddress, epicGamesPassword, epicGamesLauncherToken, fortniteClientToken, httpClient);
    }

    public static final class Builder extends Fortnite.Builder<Builder> {

        private HttpClient httpClient = HttpClientBuilder.create()
                .build();

        public Builder(String epicGamesEmailAddress, String epicGamesPassword) {
            super(epicGamesEmailAddress, epicGamesPassword);
        }

        public Builder addHttpClient(HttpClient httpClient) {
            this.httpClient = Objects.requireNonNull(httpClient, "httpClient cannot be null");
            return this;
        }

        @Override
        public Fortnite build() {
            return new ApacheFortnite(this);
        }

        @Override
        Builder self() {
            return this;
        }

        @Override
        public String toString() {
            return "Builder{" +
                    "httpClient=" + httpClient +
                    "} " + super.toString();
        }

        @Override
        public boolean equals(Object object) {
            if (this == object)
                return true;
            if (!(object instanceof Builder))
                return false;
            if (!super.equals(object))
                return false;
            Builder builder = (Builder) object;
            return httpClient.equals(builder.httpClient);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), httpClient);
        }
    }
}
