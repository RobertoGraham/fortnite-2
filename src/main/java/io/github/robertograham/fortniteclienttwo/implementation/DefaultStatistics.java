package io.github.robertograham.fortniteclienttwo.implementation;

import io.github.robertograham.fortniteclienttwo.resource.Statistics;
import org.apache.http.client.HttpClient;

import java.util.function.Supplier;

final class DefaultStatistics implements Statistics {

    private final HttpClient httpClient;
    private final Supplier<String> accessTokenSupplier;
    private final Runnable refreshSessionTokenRunnable;

    private DefaultStatistics(HttpClient httpClient, Supplier<String> accessTokenSupplier, Runnable refreshSessionTokenRunnable) {
        this.httpClient = httpClient;
        this.accessTokenSupplier = accessTokenSupplier;
        this.refreshSessionTokenRunnable = refreshSessionTokenRunnable;
    }

    static DefaultStatistics newInstance(HttpClient httpClient,
                                         Supplier<String> accessTokenSupplier,
                                         Runnable refreshSessionTokenRunnable) {
        return new DefaultStatistics(httpClient, accessTokenSupplier, refreshSessionTokenRunnable);
    }
}
