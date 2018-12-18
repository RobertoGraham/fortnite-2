package io.github.robertograham.fortniteclienttwo.implementation;

import io.github.robertograham.fortniteclienttwo.resource.Statistics;
import org.apache.http.client.HttpClient;

final class DefaultStatistics implements Statistics {

    private final HttpClient httpClient;

    private DefaultStatistics(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    static DefaultStatistics newInstance(HttpClient httpClient) {
        return new DefaultStatistics(httpClient);
    }
}
