package io.github.robertograham.fortniteclienttwo.implementation;

import io.github.robertograham.fortniteclienttwo.resource.LeaderBoard;
import org.apache.http.client.HttpClient;

import java.util.function.Supplier;

final class DefaultLeaderBoard implements LeaderBoard {

    private final HttpClient httpClient;
    private final Supplier<String> accessTokenSupplier;

    private DefaultLeaderBoard(HttpClient httpClient,
                               Supplier<String> accessTokenSupplier) {
        this.httpClient = httpClient;
        this.accessTokenSupplier = accessTokenSupplier;
    }

    static DefaultLeaderBoard newInstance(HttpClient httpClient,
                                          Supplier<String> sessionTokenSupplier) {
        return new DefaultLeaderBoard(httpClient, sessionTokenSupplier);
    }
}
