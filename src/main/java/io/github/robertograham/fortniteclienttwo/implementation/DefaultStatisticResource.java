package io.github.robertograham.fortniteclienttwo.implementation;

import io.github.robertograham.fortniteclienttwo.resource.StatisticResource;
import org.apache.http.client.HttpClient;

import java.util.function.Supplier;

final class DefaultStatisticResource implements StatisticResource {

    private final HttpClient httpClient;
    private final Supplier<String> accessTokenSupplier;

    private DefaultStatisticResource(HttpClient httpClient,
                                     Supplier<String> accessTokenSupplier) {
        this.httpClient = httpClient;
        this.accessTokenSupplier = accessTokenSupplier;
    }

    static DefaultStatisticResource newInstance(HttpClient httpClient,
                                                Supplier<String> sessionTokenSupplier) {
        return new DefaultStatisticResource(httpClient, sessionTokenSupplier);
    }
}
