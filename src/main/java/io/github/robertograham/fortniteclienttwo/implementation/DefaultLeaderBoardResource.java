package io.github.robertograham.fortniteclienttwo.implementation;

import io.github.robertograham.fortniteclienttwo.resource.LeaderBoardResource;
import org.apache.http.client.HttpClient;

import java.util.function.Supplier;

final class DefaultLeaderBoardResource implements LeaderBoardResource {

    private final HttpClient httpClient;
    private final OptionalResponseHandlerProvider optionalResponseHandlerProvider;
    private final Supplier<String> accessTokenSupplier;

    private DefaultLeaderBoardResource(HttpClient httpClient,
                                       OptionalResponseHandlerProvider optionalResponseHandlerProvider,
                                       Supplier<String> accessTokenSupplier) {
        this.httpClient = httpClient;
        this.optionalResponseHandlerProvider = optionalResponseHandlerProvider;
        this.accessTokenSupplier = accessTokenSupplier;
    }

    static DefaultLeaderBoardResource newInstance(HttpClient httpClient,
                                                  OptionalResponseHandlerProvider optionalResponseHandlerProvider,
                                                  Supplier<String> sessionTokenSupplier) {
        return new DefaultLeaderBoardResource(
                httpClient,
                optionalResponseHandlerProvider,
                sessionTokenSupplier
        );
    }
}
