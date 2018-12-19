package io.github.robertograham.fortniteclienttwo.implementation;

import io.github.robertograham.fortniteclienttwo.resource.LeaderBoardResource;
import org.apache.http.client.HttpClient;

import java.util.function.Supplier;

final class DefaultLeaderBoardResource implements LeaderBoardResource {

    private final HttpClient httpClient;
    private final Supplier<String> accessTokenSupplier;

    private DefaultLeaderBoardResource(HttpClient httpClient,
                                       Supplier<String> accessTokenSupplier) {
        this.httpClient = httpClient;
        this.accessTokenSupplier = accessTokenSupplier;
    }

    static DefaultLeaderBoardResource newInstance(HttpClient httpClient,
                                                  Supplier<String> sessionTokenSupplier) {
        return new DefaultLeaderBoardResource(httpClient, sessionTokenSupplier);
    }
}
