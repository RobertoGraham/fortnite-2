package io.github.robertograham.fortniteclienttwo.implementation;

import io.github.robertograham.fortniteclienttwo.resource.StatisticsResource;
import org.apache.http.client.HttpClient;

import java.util.function.Supplier;

final class DefaultStatisticsResource implements StatisticsResource {

    private final HttpClient httpClient;
    private final Supplier<String> accessTokenSupplier;

    private DefaultStatisticsResource(HttpClient httpClient,
                                      Supplier<String> accessTokenSupplier) {
        this.httpClient = httpClient;
        this.accessTokenSupplier = accessTokenSupplier;
    }

    static DefaultStatisticsResource newInstance(HttpClient httpClient,
                                                 Supplier<String> sessionTokenSupplier) {
        return new DefaultStatisticsResource(httpClient, sessionTokenSupplier);
    }
}
