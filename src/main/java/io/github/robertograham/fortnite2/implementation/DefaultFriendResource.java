package io.github.robertograham.fortnite2.implementation;

import io.github.robertograham.fortnite2.resource.FriendResource;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Supplier;

import static org.apache.http.HttpHeaders.AUTHORIZATION;

final class DefaultFriendResource implements FriendResource {

    private final CloseableHttpClient httpClient;
    private final OptionalResponseHandlerProvider optionalResponseHandlerProvider;
    private final Supplier<String> accessTokenSupplier;
    private final Supplier<String> sessionAccountIdSupplier;

    private DefaultFriendResource(final CloseableHttpClient httpClient,
                                  final OptionalResponseHandlerProvider optionalResponseHandlerProvider,
                                  final Supplier<String> accessTokenSupplier,
                                  final Supplier<String> sessionAccountIdSupplier) {
        this.httpClient = httpClient;
        this.optionalResponseHandlerProvider = optionalResponseHandlerProvider;
        this.accessTokenSupplier = accessTokenSupplier;
        this.sessionAccountIdSupplier = sessionAccountIdSupplier;
    }

    static DefaultFriendResource newInstance(final CloseableHttpClient httpClient,
                                             final OptionalResponseHandlerProvider optionalResponseHandlerProvider,
                                             final Supplier<String> sessionTokenSupplier,
                                             final Supplier<String> sessionAccountIdSupplier) {
        return new DefaultFriendResource(
            httpClient,
            optionalResponseHandlerProvider,
            sessionTokenSupplier,
            sessionAccountIdSupplier
        );
    }

    @Override
    public Optional<String> findAllBySessionAccountId() throws IOException {
        return httpClient.execute(
            RequestBuilder.get(String.format(
                "%s/%s",
                "https://friends-public-service-prod06.ol.epicgames.com/friends/api/public/friends",
                sessionAccountIdSupplier.get()
            ))
                .setHeader(AUTHORIZATION, "bearer " + accessTokenSupplier.get())
                .build(),
            optionalResponseHandlerProvider.forString()
        );
    }
}
