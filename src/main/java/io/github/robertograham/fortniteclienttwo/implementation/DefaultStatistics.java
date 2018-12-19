package io.github.robertograham.fortniteclienttwo.implementation;

import io.github.robertograham.fortniteclienttwo.resource.Statistics;
import org.apache.http.client.HttpClient;

import java.util.function.Supplier;

final class DefaultStatistics implements Statistics {

    private final HttpClient httpClient;
    private final Supplier<String> accessTokenSupplier;

    private DefaultStatistics(HttpClient httpClient,
                           Supplier<String> accessTokenSupplier) {
        this.httpClient = httpClient;
        this.accessTokenSupplier = accessTokenSupplier;
    }

    static DefaultStatistics newInstance(HttpClient httpClient,
                                      Supplier<String> sessionTokenSupplier) {
        return new DefaultStatistics(httpClient, sessionTokenSupplier);
    }
}
