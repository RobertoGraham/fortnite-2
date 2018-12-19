package io.github.robertograham.fortniteclienttwo.implementation;

import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Optional;

final class Authentication {

    private static final NameValuePair GRANT_TYPE_PASSWORD = new BasicNameValuePair("grant_type", "password");
    private static final NameValuePair INCLUDE_PERMS_TRUE = new BasicNameValuePair("includePerms", String.valueOf(true));
    private final HttpClient httpClient;

    private Authentication(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    static Authentication newInstance(HttpClient httpClient) {
        return new Authentication(httpClient);
    }

    Optional<Token> passwordGrantToken(String epicGamesEmailAddress, String epicGamesPassword, String epicGamesLauncherToken) throws IOException {
        final HttpPost httpPost = new HttpPost(URI.create("https://account-public-service-prod03.ol.epicgames.com/account/api/oauth/token"));
        httpPost.setHeader(HttpHeaders.AUTHORIZATION, String.format("basic %s", epicGamesLauncherToken));
        httpPost.setEntity(
                new UrlEncodedFormEntity(
                        () -> Arrays.asList(
                                GRANT_TYPE_PASSWORD,
                                INCLUDE_PERMS_TRUE,
                                new BasicNameValuePair("username", epicGamesEmailAddress),
                                new BasicNameValuePair("password", epicGamesPassword)
                        )
                                .iterator()
                )
        );
        return httpClient.execute(
                httpPost,
                ResponseHandlerProvider.INSTANCE.responseHandlerForClass(Token.class)
        );
    }
}
