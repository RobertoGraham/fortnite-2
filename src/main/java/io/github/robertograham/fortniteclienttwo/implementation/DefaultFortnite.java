package io.github.robertograham.fortniteclienttwo.implementation;

import io.github.robertograham.fortniteclienttwo.client.Fortnite;
import io.github.robertograham.fortniteclienttwo.resource.AccountResource;
import io.github.robertograham.fortniteclienttwo.resource.LeaderBoardResource;
import io.github.robertograham.fortniteclienttwo.resource.StatisticResource;
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
    private final AccountResource accountResource;
    private final AuthenticationResource authenticationResource;
    private final LeaderBoardResource leaderBoardResource;
    private final StatisticResource statisticResource;
    private Token sessionToken;

    private DefaultFortnite(Builder builder) throws IOException {
        epicGamesEmailAddress = builder.epicGamesEmailAddress;
        epicGamesPassword = builder.epicGamesPassword;
        epicGamesLauncherToken = builder.epicGamesLauncherToken;
        fortniteClientToken = builder.fortniteClientToken;
        httpClient = HttpClientBuilder.create()
                .build();
        authenticationResource = AuthenticationResource.newInstance(httpClient);
        sessionToken = fetchSessionToken();
        accountResource = DefaultAccountResource.newInstance(httpClient, this::nonExpiredAccessToken);
        leaderBoardResource = DefaultLeaderBoardResource.newInstance(httpClient, this::nonExpiredAccessToken);
        statisticResource = DefaultStatisticResource.newInstance(httpClient, this::nonExpiredAccessToken);
    }

    private Token fetchSessionToken() throws IOException {
        final String accessToken = authenticationResource.passwordGrantedToken(
                epicGamesEmailAddress,
                epicGamesPassword,
                epicGamesLauncherToken
        )
                .map(Token::accessToken)
                .orElseThrow(() -> new IllegalStateException("Couldn't retrieve an access token"));
        final String exchangeCode = authenticationResource.accessTokenGrantedExchange(accessToken)
                .map(Exchange::code)
                .orElseThrow(() -> new IllegalStateException("Couldn't retrieve an exchange code"));
        return authenticationResource.exchangeCodeGrantedToken(
                exchangeCode,
                fortniteClientToken
        )
                .orElseThrow(() -> new IllegalStateException("Couldn't establish a session"));
    }

    private String nonExpiredAccessToken() {
        if (sessionToken.isExpired())
            refreshSessionToken();
        return sessionToken.accessToken();
    }

    private void refreshSessionToken() {
        try {
            authenticationResource.refreshTokenGrantedToken(
                    sessionToken.refreshToken(),
                    fortniteClientToken
            )
                    .ifPresent(refreshTokenGrantedToken ->
                            this.sessionToken = refreshTokenGrantedToken
                    );
        } catch (IOException ignored) {
        }
    }

    @Override
    public AccountResource account() {
        return accountResource;
    }

    @Override
    public LeaderBoardResource leaderBoard() {
        return leaderBoardResource;
    }

    @Override
    public StatisticResource statistic() {
        return statisticResource;
    }

    @Override
    public void close() {
        // TODO log exceptions
        try {
            authenticationResource.retireAccessToken(nonExpiredAccessToken());
        } catch (IOException ignored) {
        }
        try {
            httpClient.close();
        } catch (IOException ignored) {
        }
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

        public Fortnite build() {
            try {
                return new DefaultFortnite(this);
            } catch (IOException e) {
                throw new IllegalStateException("Authorisation error occurred when establishing session", e);
            }
        }
    }
}
