package io.github.robertograham.fortnite2.implementation;

import io.github.robertograham.fortnite2.domain.Token;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.apache.http.entity.ContentType.APPLICATION_FORM_URLENCODED;

final class AuthenticationResource {

    private static final NameValuePair GRANT_TYPE_PASSWORD_PARAMETER = new BasicNameValuePair("grant_type", "password");
    private static final NameValuePair GRANT_TYPE_EXCHANGE_CODE_PARAMETER = new BasicNameValuePair("grant_type", "exchange_code");
    private static final NameValuePair GRANT_TYPE_REFRESH_TOKEN_PARAMETER = new BasicNameValuePair("grant_type", "refresh_token");
    private static final NameValuePair GRANT_TYPE_OTP_PARAMETER = new BasicNameValuePair("grant_type", "otp");
    private static final NameValuePair TOKEN_TYPE_EG1 = new BasicNameValuePair("token_type", "eg1");
    private final CloseableHttpClient httpClient;
    private final OptionalResponseHandlerProvider optionalResponseHandlerProvider;

    private AuthenticationResource(final CloseableHttpClient httpClient,
                                   final OptionalResponseHandlerProvider optionalResponseHandlerProvider) {
        this.httpClient = httpClient;
        this.optionalResponseHandlerProvider = optionalResponseHandlerProvider;
    }

    static AuthenticationResource newInstance(final CloseableHttpClient httpClient,
                                              final OptionalResponseHandlerProvider optionalResponseHandlerProvider) {
        return new AuthenticationResource(
            httpClient,
            optionalResponseHandlerProvider
        );
    }

    private Optional<Token> postForToken(final String bearerToken,
                                         final NameValuePair... formParameters) throws IOException {
        return httpClient.execute(
            RequestBuilder.post("https://account-public-service-prod03.ol.epicgames.com/account/api/oauth/token")
                .setHeader(AUTHORIZATION, "basic " + bearerToken)
                .setEntity(EntityBuilder.create()
                    .setContentType(APPLICATION_FORM_URLENCODED)
                    .setParameters(formParameters)
                    .build())
                .build(),
            optionalResponseHandlerProvider.forClass(DefaultToken.class)
        )
            .map(Function.identity());
    }

    Optional<Token> passwordGrantedToken(final String epicGamesEmailAddress,
                                         final String epicGamesPassword,
                                         final String epicGamesLauncherToken) throws IOException {
        return postForToken(
            epicGamesLauncherToken,
            GRANT_TYPE_PASSWORD_PARAMETER,
            new BasicNameValuePair("username", epicGamesEmailAddress),
            new BasicNameValuePair("password", epicGamesPassword)
        );
    }

    Optional<Exchange> accessTokenGrantedExchange(final String accessToken) throws IOException {
        return httpClient.execute(
            RequestBuilder.get("https://account-public-service-prod03.ol.epicgames.com/account/api/oauth/exchange")
                .setHeader(AUTHORIZATION, "bearer " + accessToken)
                .build(),
            optionalResponseHandlerProvider.forClass(Exchange.class)
        );
    }

    Optional<Token> exchangeCodeGrantedToken(final String exchangeCode,
                                             final String fortniteClientToken) throws IOException {
        return postForToken(
            fortniteClientToken,
            GRANT_TYPE_EXCHANGE_CODE_PARAMETER,
            TOKEN_TYPE_EG1,
            new BasicNameValuePair("exchange_code", exchangeCode)
        );
    }

    Optional<Token> refreshTokenGrantedToken(final String refreshToken,
                                             final String fortniteClientToken) throws IOException {
        return postForToken(
            fortniteClientToken,
            GRANT_TYPE_REFRESH_TOKEN_PARAMETER,
            new BasicNameValuePair("refresh_token", refreshToken)
        );
    }

    Optional<Token> twoFactorAuthenticationCodeGrantedToken(final String epicGamesLauncherToken,
                                                            final String challenge,
                                                            final String twoFactorAuthenticationCode) throws IOException {
        return postForToken(
            epicGamesLauncherToken,
            GRANT_TYPE_OTP_PARAMETER,
            new BasicNameValuePair("otp", twoFactorAuthenticationCode),
            new BasicNameValuePair("challenge", challenge)
        );
    }

    void retireAccessToken(final String accessToken) throws IOException {
        httpClient.execute(
            RequestBuilder.delete("https://account-public-service-prod03.ol.epicgames.com/account/api/oauth/sessions/kill/" + accessToken)
                .setHeader(AUTHORIZATION, "bearer " + accessToken)
                .build(),
            optionalResponseHandlerProvider.forString()
        );
    }

    Optional<Eula> getEula(final String accessToken, final String accountId) throws IOException {
        return httpClient.execute(
            RequestBuilder.get(String.format(
                "%s/%s",
                "https://eulatracking-public-service-prod-m.ol.epicgames.com/eulatracking/api/public/agreements/fn/account",
                accountId
            ))
                .setHeader(AUTHORIZATION, "bearer " + accessToken)
                .addParameter("locale", "en-US")
                .build(),
            optionalResponseHandlerProvider.forClass(Eula.class)
        );
    }

    void acceptEula(final String accessToken, final String accountId, final long eulaVersion) throws IOException {
        httpClient.execute(
            RequestBuilder.post(String.format(
                "%s/%d/%s/%s/%s",
                "https://eulatracking-public-service-prod-m.ol.epicgames.com/eulatracking/api/public/agreements/fn/version",
                eulaVersion,
                "account",
                accountId,
                "accept"
            ))
                .setHeader(AUTHORIZATION, "bearer " + accessToken)
                .addParameter("locale", "en")
                .build(),
            optionalResponseHandlerProvider.forString()
        );
    }

    void grantAccess(final String accessToken, final String accountId) throws IOException {
        httpClient.execute(
            RequestBuilder.post(String.format(
                "%s/%s",
                "https://fortnite-public-service-prod11.ol.epicgames.com/fortnite/api/game/v2/grant_access",
                accountId
            ))
                .setHeader(AUTHORIZATION, "bearer " + accessToken)
                .build(),
            optionalResponseHandlerProvider.forString()
        );
    }

    void killOtherSessions(final String accessToken) throws IOException {
        httpClient.execute(
            RequestBuilder.delete("https://account-public-service-prod03.ol.epicgames.com/account/api/oauth/sessions/kill")
                .addParameter("killType", "OTHERS_ACCOUNT_CLIENT_SERVICE")
                .setHeader(AUTHORIZATION, "bearer " + accessToken)
                .build(),
            optionalResponseHandlerProvider.forString()
        );
    }
}
