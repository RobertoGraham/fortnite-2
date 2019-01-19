package io.github.robertograham.fortnite2.implementation;

import io.github.robertograham.fortnite2.domain.FriendRequest;
import io.github.robertograham.fortnite2.resource.FriendRequestResource;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static org.apache.http.HttpHeaders.AUTHORIZATION;

final class DefaultFriendRequestResource implements FriendRequestResource {

    private final CloseableHttpClient httpClient;
    private final OptionalResponseHandlerProvider optionalResponseHandlerProvider;
    private final Supplier<String> accessTokenSupplier;
    private final Supplier<String> sessionAccountIdSupplier;

    private DefaultFriendRequestResource(final CloseableHttpClient httpClient,
                                         final OptionalResponseHandlerProvider optionalResponseHandlerProvider,
                                         final Supplier<String> accessTokenSupplier,
                                         final Supplier<String> sessionAccountIdSupplier) {
        this.httpClient = httpClient;
        this.optionalResponseHandlerProvider = optionalResponseHandlerProvider;
        this.accessTokenSupplier = accessTokenSupplier;
        this.sessionAccountIdSupplier = sessionAccountIdSupplier;
    }

    static DefaultFriendRequestResource newInstance(final CloseableHttpClient httpClient,
                                                    final OptionalResponseHandlerProvider optionalResponseHandlerProvider,
                                                    final Supplier<String> sessionTokenSupplier,
                                                    final Supplier<String> sessionAccountIdSupplier) {
        return new DefaultFriendRequestResource(
            httpClient,
            optionalResponseHandlerProvider,
            sessionTokenSupplier,
            sessionAccountIdSupplier
        );
    }

    private Optional<List<FriendRequest>> findAllBySessionAccountId(final boolean includePending) throws IOException {
        return httpClient.execute(
            RequestBuilder.get(String.format(
                "%s/%s",
                "https://friends-public-service-prod06.ol.epicgames.com/friends/api/public/friends",
                sessionAccountIdSupplier.get()
            ))
                .addParameter("includePending", Boolean.toString(includePending))
                .setHeader(AUTHORIZATION, "bearer " + accessTokenSupplier.get())
                .build(),
            optionalResponseHandlerProvider.forClass(DefaultFriendRequest[].class)
        )
            .map(Arrays::asList);
    }

    @Override
    public Optional<List<FriendRequest>> findAllBySessionAccountId() throws IOException {
        return findAllBySessionAccountId(true);
    }

    @Override
    public Optional<List<FriendRequest>> findAllNonPendingBySessionAccountId() throws IOException {
        return findAllBySessionAccountId(false);
    }
}
