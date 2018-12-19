package io.github.robertograham.fortniteclienttwo.implementation;

import io.github.robertograham.fortniteclienttwo.client.Fortnite;
import io.github.robertograham.fortniteclienttwo.resource.Account;
import io.github.robertograham.fortniteclienttwo.resource.LeaderBoard;
import io.github.robertograham.fortniteclienttwo.resource.Statistics;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.Objects;

public final class DefaultFortnite implements Fortnite {

    private final String epicGamesEmailAddress;
    private final String epicGamesPassword;
    private final String epicGamesLauncherToken;
    private final String fortniteClientToken;
    private final HttpClient httpClient;
    private final Account account;
    private final Authentication authentication;
    private final LeaderBoard leaderBoard;
    private final Statistics statistics;

    private DefaultFortnite(Builder builder) {
        epicGamesEmailAddress = builder.epicGamesEmailAddress;
        epicGamesPassword = builder.epicGamesPassword;
        epicGamesLauncherToken = builder.epicGamesLauncherToken;
        fortniteClientToken = builder.fortniteClientToken;
        httpClient = HttpClientBuilder.create()
                .build();
        account = DefaultAccount.newInstance(httpClient);
        authentication = Authentication.newInstance(httpClient);
        leaderBoard = DefaultLeaderBoard.newInstance(httpClient);
        statistics = DefaultStatistics.newInstance(httpClient);
    }

    @Override
    public Account account() {
        return account;
    }

    @Override
    public LeaderBoard leaderBoard() {
        return leaderBoard;
    }

    @Override
    public Statistics statistics() {
        return statistics;
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

        public Builder setEpicGamesLauncherToken(String epicGamesLauncherToken) {
            this.epicGamesLauncherToken = Objects.requireNonNull(epicGamesLauncherToken, "epicGamesLauncherToken cannot be null");
            return this;
        }

        public Builder setFortniteClientToken(String fortniteClientToken) {
            this.fortniteClientToken = Objects.requireNonNull(fortniteClientToken, "fortniteClientToken cannot be null");
            return this;
        }

        public DefaultFortnite build() {
            return new DefaultFortnite(this);
        }
    }
}
