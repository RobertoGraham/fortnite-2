package io.github.robertograham.fortniteclienttwo.implementation;

import io.github.robertograham.fortniteclienttwo.resource.LeaderBoard;
import org.apache.http.client.HttpClient;

import java.util.function.Supplier;

final class DefaultLeaderBoard implements LeaderBoard {

    private final HttpClient httpClient;
    private final Supplier<String> accessTokenSupplier;
    private final Runnable refreshSessionTokenRunnable;

    private DefaultLeaderBoard(HttpClient httpClient, Supplier<String> accessTokenSupplier, Runnable refreshSessionTokenRunnable) {
        this.httpClient = httpClient;
        this.accessTokenSupplier = accessTokenSupplier;
        this.refreshSessionTokenRunnable = refreshSessionTokenRunnable;
    }

    static DefaultLeaderBoard newInstance(HttpClient httpClient,
                                          Supplier<String> accessTokenSupplier,
                                          Runnable refreshSessionTokenRunnable) {
        return new DefaultLeaderBoard(httpClient, accessTokenSupplier, refreshSessionTokenRunnable);
    }
}
