package io.github.robertograham.fortniteclienttwo.implementation;

import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Optional;

final class AuthenticationResource {

    private static final URI TOKEN_URI = URI.create("https://account-public-service-prod03.ol.epicgames.com/account/api/oauth/token");
    private static final URI EXCHANGE_URI = URI.create("https://account-public-service-prod03.ol.epicgames.com/account/api/oauth/exchange");
    private static final URI KILL_TOKEN_URI = URI.create("https://account-public-service-prod03.ol.epicgames.com/account/api/oauth/sessions/kill");
    private static final NameValuePair GRANT_TYPE_PASSWORD = new BasicNameValuePair("grant_type", "password");
    private static final NameValuePair GRANT_TYPE_EXCHANGE_CODE = new BasicNameValuePair("grant_type", "exchange_code");
    private static final NameValuePair GRANT_TYPE_REFRESH_TOKEN = new BasicNameValuePair("grant_type", "refresh_token");
    private static final NameValuePair TOKEN_TYPE_EG1 = new BasicNameValuePair("token_type", "eg1");
    private final HttpClient httpClient;
    private final OptionalResponseHandlerProvider optionalResponseHandlerProvider;

    private AuthenticationResource(HttpClient httpClient,
                                   OptionalResponseHandlerProvider optionalResponseHandlerProvider) {
        this.httpClient = httpClient;
        this.optionalResponseHandlerProvider = optionalResponseHandlerProvider;
    }

    static AuthenticationResource newInstance(HttpClient httpClient,
                                              OptionalResponseHandlerProvider optionalResponseHandlerProvider) {
        return new AuthenticationResource(
                httpClient,
                optionalResponseHandlerProvider
        );
    }

    private Optional<Token> postForToken(String authorizationHeaderToken,
                                         NameValuePair... urlEncodedFormParts) throws IOException {
        final HttpPost httpPost = new HttpPost(TOKEN_URI);
        httpPost.setHeader(HttpHeaders.AUTHORIZATION, String.format("basic %s", authorizationHeaderToken));
        httpPost.setEntity(
                new UrlEncodedFormEntity(
                        Arrays.asList(urlEncodedFormParts)
                )
        );
        return httpClient.execute(
                httpPost,
                optionalResponseHandlerProvider.forClass(Token.class)
        );
    }

    Optional<Token> passwordGrantedToken(String epicGamesEmailAddress, String epicGamesPassword, String epicGamesLauncherToken) throws IOException {
        return postForToken(
                epicGamesLauncherToken,
                GRANT_TYPE_PASSWORD,
                new BasicNameValuePair("username", epicGamesEmailAddress),
                new BasicNameValuePair("password", epicGamesPassword)
        );
    }

    Optional<Exchange> accessTokenGrantedExchange(String accessToken) throws IOException {
        final HttpGet httpGet = new HttpGet(EXCHANGE_URI);
        httpGet.setHeader(HttpHeaders.AUTHORIZATION, String.format("bearer %s", accessToken));
        return httpClient.execute(
                httpGet,
                optionalResponseHandlerProvider.forClass(Exchange.class)
        );
    }

    Optional<Token> exchangeCodeGrantedToken(String exchangeCode, String fortniteClientToken) throws IOException {
        return postForToken(
                fortniteClientToken,
                GRANT_TYPE_EXCHANGE_CODE,
                TOKEN_TYPE_EG1,
                new BasicNameValuePair("exchange_code", exchangeCode)
        );
    }

    Optional<Token> refreshTokenGrantedToken(String refreshToken, String fortniteClientToken) throws IOException {
        return postForToken(
                fortniteClientToken,
                GRANT_TYPE_REFRESH_TOKEN,
                new BasicNameValuePair("refresh_token", refreshToken)
        );
    }

    void retireAccessToken(String accessToken) throws IOException {
        final HttpDelete httpDelete = new HttpDelete(
                String.format(
                        "%s/%s",
                        KILL_TOKEN_URI,
                        accessToken
                )
        );
        httpDelete.setHeader(HttpHeaders.AUTHORIZATION, String.format("bearer %s", accessToken));
        httpClient.execute(
                httpDelete,
                optionalResponseHandlerProvider.forString()
        );
    }
}
