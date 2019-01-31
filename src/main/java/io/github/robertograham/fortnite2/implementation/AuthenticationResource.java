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

    void retireAccessToken(final String accessToken) throws IOException {
        httpClient.execute(
            RequestBuilder.delete("https://account-public-service-prod03.ol.epicgames.com/account/api/oauth/sessions/kill/" + accessToken)
                .setHeader(AUTHORIZATION, "bearer " + accessToken)
                .build(),
            optionalResponseHandlerProvider.forString()
        );
    }
}
