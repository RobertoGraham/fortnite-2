package io.github.robertograham.fortniteclienttwo.implementation;

import io.github.robertograham.fortniteclienttwo.resource.StatisticResource;
import org.apache.http.client.HttpClient;

import java.util.function.Supplier;

final class DefaultStatisticResource implements StatisticResource {

    private final HttpClient httpClient;
    private final OptionalResponseHandlerProvider optionalResponseHandlerProvider;
    private final Supplier<String> accessTokenSupplier;

    private DefaultStatisticResource(HttpClient httpClient,
                                     OptionalResponseHandlerProvider optionalResponseHandlerProvider,
                                     Supplier<String> accessTokenSupplier) {
        this.httpClient = httpClient;
        this.optionalResponseHandlerProvider = optionalResponseHandlerProvider;
        this.accessTokenSupplier = accessTokenSupplier;
    }

    static DefaultStatisticResource newInstance(HttpClient httpClient,
                                                OptionalResponseHandlerProvider optionalResponseHandlerProvider,
                                                Supplier<String> sessionTokenSupplier) {
        return new DefaultStatisticResource(
                httpClient,
                optionalResponseHandlerProvider,
                sessionTokenSupplier
        );
    }
}
