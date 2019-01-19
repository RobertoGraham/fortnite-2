package io.github.robertograham.fortnite2.implementation;

import io.github.robertograham.fortnite2.client.Fortnite;
import io.github.robertograham.fortnite2.resource.AccountResource;
import io.github.robertograham.fortnite2.resource.FriendResource;
import io.github.robertograham.fortnite2.resource.LeaderBoardResource;
import io.github.robertograham.fortnite2.resource.StatisticResource;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.Objects;

import static java.time.LocalDateTime.now;

/**
 * Default implementation of {@link Fortnite} created using {@link Builder}
 */
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
    private final FriendResource friendResource;
    private Token sessionToken;

    private DefaultFortnite(final Builder builder) throws IOException {
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
            () -> nonExpiredSessionToken().accessToken(),
            () -> nonExpiredSessionToken().accountId()
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
            () -> nonExpiredSessionToken().accessToken(),
            () -> nonExpiredSessionToken().accountId()
        );
        friendResource = DefaultFriendResource.newInstance(
            httpClient,
            JsonOptionalResponseHandlerProvider.INSTANCE,
            () -> nonExpiredSessionToken().accessToken(),
            () -> nonExpiredSessionToken().accountId()
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
            final String oldAccessToken = sessionToken.accessToken();
            sessionToken = fetchSessionToken();
            authenticationResource.retireAccessToken(oldAccessToken);
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
    }

    private void refreshSession() {
        try {
            final String oldAccessToken = sessionToken.accessToken();
            sessionToken = authenticationResource.refreshTokenGrantedToken(
                sessionToken.refreshToken(),
                fortniteClientToken
            )
                .orElseThrow(() -> new IOException("Couldn't refresh session"));
            authenticationResource.retireAccessToken(oldAccessToken);
        } catch (final IOException exception) {
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
    public FriendResource friend() {
        return friendResource;
    }

    @Override
    public void close() {
        // TODO log exceptions
        try {
            authenticationResource.retireAccessToken(nonExpiredSessionToken().accessToken());
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
        HttpClientUtils.closeQuietly(httpClient);
    }

    /**
     * Used to create {@link Fortnite} instances
     * Instantiated using {@link #newInstance(String, String)}
     */
    public static final class Builder {

        private final String epicGamesEmailAddress;
        private final String epicGamesPassword;
        private String epicGamesLauncherToken = "MzQ0NmNkNzI2OTRjNGE0NDg1ZDgxYjc3YWRiYjIxNDE6OTIwOWQ0YTVlMjVhNDU3ZmI5YjA3NDg5ZDMxM2I0MWE=";
        private String fortniteClientToken = "ZWM2ODRiOGM2ODdmNDc5ZmFkZWEzY2IyYWQ4M2Y1YzY6ZTFmMzFjMjExZjI4NDEzMTg2MjYyZDM3YTEzZmM4NGQ=";

        private Builder(final String epicGamesEmailAddress, final String epicGamesPassword) {
            this.epicGamesEmailAddress = epicGamesEmailAddress;
            this.epicGamesPassword = epicGamesPassword;
        }

        /**
         * @param epicGamesEmailAddress email address used to log in
         * @param epicGamesPassword     password used to log in
         * @return a new {@link Builder} instance
         * @throws NullPointerException if {@code epicGamesEmailAddress} is {@code null}
         * @throws NullPointerException if {@code epicGamesPassword} is {@code null}
         */
        public static Builder newInstance(final String epicGamesEmailAddress,
                                          final String epicGamesPassword) {
            Objects.requireNonNull(epicGamesEmailAddress, "epicGamesEmailAddress cannot be null");
            Objects.requireNonNull(epicGamesPassword, "epicGamesPassword cannot be null");
            return new Builder(epicGamesEmailAddress, epicGamesPassword);
        }

        /**
         * @param epicGamesLauncherToken token used by Epic Games Launcher
         * @return the {@link Builder} instance this was called on
         * @throws NullPointerException if {@code epicGamesLauncherToken} is {@code null}
         */
        public Builder setEpicGamesLauncherToken(final String epicGamesLauncherToken) {
            this.epicGamesLauncherToken = Objects.requireNonNull(epicGamesLauncherToken, "epicGamesLauncherToken cannot be null");
            return this;
        }

        /**
         * @param fortniteClientToken token used by the Fortnite client
         * @return the {@link Builder} instance this was called on
         * @throws NullPointerException if {@code fortniteClientToken} is {@code null}
         */
        public Builder setFortniteClientToken(final String fortniteClientToken) {
            this.fortniteClientToken = Objects.requireNonNull(fortniteClientToken, "fortniteClientToken cannot be null");
            return this;
        }

        /**
         * @return a new instance of {@link Fortnite}
         * @throws IllegalStateException if there's a problem logging in
         */
        public Fortnite build() {
            try {
                return new DefaultFortnite(this);
            } catch (final IOException exception) {
                throw new IllegalStateException("Error occurred when establishing session", exception);
            }
        }
    }
}
