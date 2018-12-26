package io.github.robertograham.fortnite2.implementation;

import io.github.robertograham.fortnite2.client.Fortnite;
import io.github.robertograham.fortnite2.resource.AccountResource;
import io.github.robertograham.fortnite2.resource.LeaderBoardResource;
import io.github.robertograham.fortnite2.resource.StatisticResource;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.Objects;

import static java.time.LocalDateTime.now;

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
        httpClient = HttpClients.createDefault();
        authenticationResource = AuthenticationResource.newInstance(
                httpClient,
                JsonOptionalResponseHandlerProvider.INSTANCE
        );
        sessionToken = fetchSessionToken();
        accountResource = DefaultAccountResource.newInstance(
                httpClient,
                JsonOptionalResponseHandlerProvider.INSTANCE,
                () -> nonExpiredSessionToken().accessToken()
        );
        leaderBoardResource = DefaultLeaderBoardResource.newInstance(
                httpClient,
                JsonOptionalResponseHandlerProvider.INSTANCE,
                () -> nonExpiredSessionToken().accessToken(),
                () -> nonExpiredSessionToken().inAppId()
        );
        statisticResource = DefaultStatisticResource.newInstance(
                httpClient,
                JsonOptionalResponseHandlerProvider.INSTANCE,
                () -> nonExpiredSessionToken().accessToken()
        );
    }

    private Token fetchSessionToken() throws IOException {
        final String accessToken = authenticationResource.passwordGrantedToken(
                epicGamesEmailAddress,
                epicGamesPassword,
                epicGamesLauncherToken
        )
                .map(Token::accessToken)
                .orElseThrow(() -> new IOException("Couldn't retrieve an access token"));
        final String exchangeCode = authenticationResource.accessTokenGrantedExchange(accessToken)
                .map(Exchange::code)
                .orElseThrow(() -> new IOException("Couldn't retrieve an exchange code"));
        return authenticationResource.exchangeCodeGrantedToken(
                exchangeCode,
                fortniteClientToken
        )
                .orElseThrow(() -> new IOException("Couldn't establish a session"));
    }

    private Token nonExpiredSessionToken() {
        if (sessionToken.refreshExpiresAt()
                .minusMinutes(5L)
                .isBefore(now())) {
            establishNewSession();
        }
        if (sessionToken.expiresAt()
                .minusMinutes(5L)
                .isBefore(now()))
            refreshSession();
        return sessionToken;
    }

    private void establishNewSession() {
        try {
            String oldAccessToken = sessionToken.accessToken();
            sessionToken = fetchSessionToken();
            authenticationResource.retireAccessToken(oldAccessToken);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void refreshSession() {
        try {
            String oldAccessToken = sessionToken.accessToken();
            sessionToken = authenticationResource.refreshTokenGrantedToken(
                    sessionToken.refreshToken(),
                    fortniteClientToken
            )
                    .orElseThrow(() -> new IOException("Couldn't refresh session"));
            authenticationResource.retireAccessToken(oldAccessToken);
        } catch (IOException exception) {
            exception.printStackTrace();
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
            authenticationResource.retireAccessToken(nonExpiredSessionToken().accessToken());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        try {
            httpClient.close();
        } catch (IOException exception) {
            exception.printStackTrace();
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
            } catch (IOException exception) {
                throw new IllegalStateException("Error occurred when establishing session", exception);
            }
        }
    }
}
