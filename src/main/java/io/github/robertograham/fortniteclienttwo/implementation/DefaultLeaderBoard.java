package io.github.robertograham.fortniteclienttwo.implementation;

import io.github.robertograham.fortniteclienttwo.resource.LeaderBoard;
import org.apache.http.client.HttpClient;

final class DefaultLeaderBoard implements LeaderBoard {

    private final HttpClient httpClient;

    private DefaultLeaderBoard(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    static DefaultLeaderBoard newInstance(HttpClient httpClient) {
        return new DefaultLeaderBoard(httpClient);
    }
}
