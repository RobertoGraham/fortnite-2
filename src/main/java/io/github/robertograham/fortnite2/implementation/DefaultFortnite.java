package io.github.robertograham.fortnite2.implementation;

import io.github.robertograham.fortnite2.client.Fortnite;
import io.github.robertograham.fortnite2.domain.Token;
import io.github.robertograham.fortnite2.resource.AccountResource;
import io.github.robertograham.fortnite2.resource.FriendResource;
import io.github.robertograham.fortnite2.resource.LeaderBoardResource;
import io.github.robertograham.fortnite2.resource.StatisticResource;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Default implementation of {@link Fortnite} created using {@link Builder}
 */
public final class DefaultFortnite implements Fortnite {

    private final String epicGamesEmailAddress;
    private final String epicGamesPassword;
    private final String epicGamesLauncherToken;
    private final String fortniteClientToken;
    private final String challenge;
    private final String code;
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
        challenge = builder.challenge;
        code = builder.code;
        httpClient = HttpClientBuilder.create()
            .setDefaultHeaders(Set.of(new BasicHeader("X-Epic-Device-ID", "".equals(builder.deviceId) ?
                DigestUtils.md5Hex(NetworkInterface.getByInetAddress(InetAddress.getLocalHost())
                    .getHardwareAddress())
                : builder.deviceId)))
            .build();
        authenticationResource = AuthenticationResource.newInstance(
            httpClient,
            JsonOptionalResponseHandlerProvider.INSTANCE
        );
        sessionToken = fetchSessionToken();
        if (builder.killOtherSessions)
            killOtherSessions();
        if (builder.autoAcceptEulaAndGrantAccess)
            acceptEulaIfNeededAndGrantAccess();
        accountResource = DefaultAccountResource.newInstance(
            httpClient,
            JsonOptionalResponseHandlerProvider.INSTANCE,
            () -> session().accessToken(),
            () -> session().accountId()
        );
        leaderBoardResource = DefaultLeaderBoardResource.newInstance(
            httpClient,
            JsonOptionalResponseHandlerProvider.INSTANCE,
            () -> session().accessToken(),
            () -> session().inAppId()
        );
        statisticResource = DefaultStatisticResource.newInstance(
            httpClient,
            JsonOptionalResponseHandlerProvider.INSTANCE,
            () -> session().accessToken(),
            () -> session().accountId()
        );
        friendResource = DefaultFriendResource.newInstance(
            httpClient,
            JsonOptionalResponseHandlerProvider.INSTANCE,
            () -> session().accessToken(),
            () -> session().accountId()
        );
    }

    private Token fetchSessionToken() throws IOException {
        var tokenOptional = Optional.<Token>empty();
        if (!challenge.isEmpty() && !code.isEmpty())
            tokenOptional = authenticationResource.twoFactorAuthenticationCodeGrantedToken(
                epicGamesLauncherToken,
                challenge,
                code
            );
        else
            tokenOptional = authenticationResource.passwordGrantedToken(
                epicGamesEmailAddress,
                epicGamesPassword,
                epicGamesLauncherToken
            );
        final var accessTokenString = tokenOptional.map(Token::accessToken)
            .orElseThrow(() -> new IOException("Couldn't retrieve an access token"));
        final var exchangeCodeString = authenticationResource.accessTokenGrantedExchange(accessTokenString)
            .map(Exchange::code)
            .orElseThrow(() -> new IOException("Couldn't retrieve an exchange code"));
        return authenticationResource.exchangeCodeGrantedToken(
            exchangeCodeString,
            fortniteClientToken
        )
            .orElseThrow(() -> new IOException("Couldn't establish a session"));
    }

    private void establishNewSession() {
        try {
            final var oldAccessTokenString = sessionToken.accessToken();
            sessionToken = fetchSessionToken();
            authenticationResource.retireAccessToken(oldAccessTokenString);
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
    }

    private void refreshSession() {
        try {
            final var oldAccessTokenString = sessionToken.accessToken();
            sessionToken = authenticationResource.refreshTokenGrantedToken(
                sessionToken.refreshToken(),
                fortniteClientToken
            )
                .orElseThrow(() -> new IOException("Couldn't refresh session"));
            authenticationResource.retireAccessToken(oldAccessTokenString);
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
    }

    private void acceptEulaIfNeededAndGrantAccess() throws IOException {
        try {
            final var eulaVersionLongOptional = authenticationResource.getEula(
                sessionToken.accessToken(),
                sessionToken.accountId()
            )
                .map(Eula::version);
            if (eulaVersionLongOptional.isPresent())
                authenticationResource.acceptEula(
                    sessionToken.accessToken(),
                    sessionToken.accountId(),
                    eulaVersionLongOptional.orElseThrow()
                );
        } catch (final IOException exception) {
            authenticationResource.retireAccessToken(sessionToken.accessToken());
            throw new IOException("Couldn't accept EULA", exception);
        }
        try {
            authenticationResource.grantAccess(
                sessionToken.accessToken(),
                sessionToken.accountId()
            );
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
    }

    private void killOtherSessions() throws IOException {
        authenticationResource.killOtherSessions(sessionToken.accessToken());
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
    public Token session() {
        if (sessionToken.refreshTokenExpiryTime()
            .minusMinutes(5L)
            .isBefore(LocalDateTime.now()))
            establishNewSession();
        if (sessionToken.accessTokenExpiryTime()
            .minusMinutes(5L)
            .isBefore(LocalDateTime.now()))
            refreshSession();
        return sessionToken;
    }

    @Override
    public void close() {
        try {
            authenticationResource.retireAccessToken(session().accessToken());
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
        private boolean autoAcceptEulaAndGrantAccess = false;
        private boolean killOtherSessions = false;
        private String challenge = "";
        private String code = "";
        private String deviceId = "";

        private Builder(final String epicGamesEmailAddress, final String epicGamesPassword) {
            this.epicGamesEmailAddress = epicGamesEmailAddress;
            this.epicGamesPassword = epicGamesPassword;
        }

        /**
         * Can supply two empty strings if {@link Builder#setTwoFactorAuthChallengeAndCodePair(String, String)}
         * is being called with two non-empty strings
         *
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
         * @param autoAcceptEulaAndGrantAccess {@code true} to automatically accept Fortnite's EULA for the
         *                                     authenticated user and to grant the user access to Fortnite's APIs if needed.
         *                                     {@code false} to not automatically accept Fortnite's EULA or grant the
         *                                     authenticated user access to Fortnite's APIs if needed
         * @return the {@link Builder} instance this was called on
         */
        public Builder setAutoAcceptEulaAndGrantAccess(final boolean autoAcceptEulaAndGrantAccess) {
            this.autoAcceptEulaAndGrantAccess = autoAcceptEulaAndGrantAccess;
            return this;
        }

        /**
         * @param killOtherSessions {@code true} to kill existing sessions.
         *                          {@code false} to not kill existing sessions
         * @return the {@link Builder} instance this was called on
         */
        public Builder setKillOtherSessions(final boolean killOtherSessions) {
            this.killOtherSessions = killOtherSessions;
            return this;
        }

        /**
         * @param challenge can be obtained by calling {@code jsonObject.getString("challenge")} when
         *                  {@link EpicGamesErrorException#jsonObject()} is present and
         *                  {@link EpicGamesErrorException#type()} is {@code "errors.com.epicgames.common.two_factor_authentication.required"}
         * @param code      2FA code obtained from Epic Games email inbox or authenticator app
         * @return the {@link Builder} instance this was called on
         * @throws NullPointerException if {@code challenge} is {@code null}
         * @throws NullPointerException if {@code code} is {@code null}
         */
        public Builder setTwoFactorAuthChallengeAndCodePair(final String challenge, final String code) {
            this.challenge = Objects.requireNonNull(challenge, "challenge cannot be null");
            this.code = Objects.requireNonNull(code, "code cannot be null");
            return this;
        }

        /**
         * @param deviceId if you have successfully passed 2FA using this ID before,
         *                 don't call {@link Builder#setTwoFactorAuthChallengeAndCodePair(String, String)}
         * @return the {@link Builder} instance this was called on
         * @throws NullPointerException     if {@code deviceId} is {@code null}
         * @throws IllegalArgumentException if {@code deviceId} is empty
         */
        public Builder setDeviceId(final String deviceId) {
            this.deviceId = Objects.requireNonNull(deviceId, "deviceId cannot be null");
            if (this.deviceId.isEmpty())
                throw new IllegalArgumentException("deviceId cannot be empty");
            return this;
        }

        /**
         * @return a new instance of {@link Fortnite}
         * @throws EpicGamesErrorException       if there's an error response during authentication
         * @throws java.net.SocketException      problem generating device ID. If this happens supply a non-empty device ID using {@link Builder#setDeviceId(String)}
         * @throws java.net.UnknownHostException problem generating device ID. If this happens supply a non-empty device ID using {@link Builder#setDeviceId(String)}
         * @throws IOException                   if there's a problem logging in
         */
        public Fortnite build() throws IOException {
            return new DefaultFortnite(this);
        }
    }
}
