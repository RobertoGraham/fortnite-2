package io.github.robertograham.fortniteclienttwo.implementation;

import org.apache.http.client.HttpClient;

final class Authentication {

    private final HttpClient httpClient;

    private Authentication(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    static Authentication newInstance(HttpClient httpClient) {
        return new Authentication(httpClient);
    }

    String passwordGrantToken(String epicGamesEmailAddress, String epicGamesPassword, String epicGamesLauncherToken) {
        return null;
    }
}
