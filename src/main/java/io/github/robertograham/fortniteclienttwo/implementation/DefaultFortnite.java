package io.github.robertograham.fortniteclienttwo.implementation;

import io.github.robertograham.fortniteclienttwo.client.Fortnite;
import io.github.robertograham.fortniteclienttwo.resource.Account;
import io.github.robertograham.fortniteclienttwo.resource.LeaderBoard;
import io.github.robertograham.fortniteclienttwo.resource.Statistics;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.Objects;

public final class DefaultFortnite implements Fortnite {

    private final String epicGamesEmailAddress;
    private final String epicGamesPassword;
    private final String epicGamesLauncherToken;
    private final String fortniteClientToken;
    private final CloseableHttpClient httpClient;
    private final Account account;
    private final Authentication authentication;
    private final LeaderBoard leaderBoard;
    private final Statistics statistics;
    private Token sessionToken;

    private DefaultFortnite(Builder builder) throws IOException {
        epicGamesEmailAddress = builder.epicGamesEmailAddress;
        epicGamesPassword = builder.epicGamesPassword;
        epicGamesLauncherToken = builder.epicGamesLauncherToken;
        fortniteClientToken = builder.fortniteClientToken;
        httpClient = HttpClientBuilder.create()
                .build();
        authentication = Authentication.newInstance(httpClient);
        sessionToken = fetchSessionToken();
        account = DefaultAccount.newInstance(httpClient, sessionToken::accessToken, this::refreshSessionToken);
        leaderBoard = DefaultLeaderBoard.newInstance(httpClient, sessionToken::accessToken, this::refreshSessionToken);
        statistics = DefaultStatistics.newInstance(httpClient, sessionToken::accessToken, this::refreshSessionToken);
    }

    private Token fetchSessionToken() throws IOException {
        final String accessToken = authentication.passwordGrantedToken(
                epicGamesEmailAddress,
                epicGamesPassword,
                epicGamesLauncherToken
        )
                .map(Token::accessToken)
                .orElseThrow(() -> new IllegalStateException("Couldn't retrieve an access token"));
        final String exchangeCode = authentication.accessTokenGrantedExchange(accessToken)
                .map(Exchange::code)
                .orElseThrow(() -> new IllegalStateException("Couldn't retrieve an exchange code"));
        return authentication.exchangeCodeGrantedToken(
                exchangeCode,
                fortniteClientToken
        )
                .orElseThrow(() -> new IllegalStateException("Couldn't establish a session"));
    }

    private void refreshSessionToken() {
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

    @Override
    public void close() throws IOException {
        httpClient.close();
    }

    public static final class Builder {

        private final String epicGamesEmailAddress;
        private final String epicGamesPassword;
        private String epicGamesLauncherToken = "MzQ0NmNkNzI2OTRjNGE0NDg1ZDgxYjc3YWRiYjIxNDE6OTIwOWQ0YTVlMjVhNDU3ZmI5YjA3NDg5ZDMxM2I0MWE=";
        private String fortniteClientToken = "ZWM2ODRiOGM2ODdmNDc5ZmFkZWEzY2IyYWQ4M2Y1YzY6ZTFmMzFjMjExZjI4NDEzMTg2MjYyZDM3YTEzZmM4NGQ=";

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
            try {
                return new DefaultFortnite(this);
            } catch (IOException e) {
                throw new IllegalStateException("Authorisation error occurred when trying establishing session", e);
            }
        }
    }
}
