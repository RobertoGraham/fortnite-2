package io.github.robertograham.fortnite2.implementation;

import io.github.robertograham.fortnite2.domain.FilterableStatistic;
import io.github.robertograham.fortnite2.resource.StatisticResource;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import static org.apache.http.HttpHeaders.AUTHORIZATION;

final class DefaultStatisticResource implements StatisticResource {

    private final CloseableHttpClient httpClient;
    private final OptionalResponseHandlerProvider optionalResponseHandlerProvider;
    private final Supplier<String> accessTokenSupplier;
    private final Supplier<String> sessionAccountIdSupplier;

    private DefaultStatisticResource(final CloseableHttpClient httpClient,
                                     final OptionalResponseHandlerProvider optionalResponseHandlerProvider,
                                     final Supplier<String> accessTokenSupplier,
                                     final Supplier<String> sessionAccountIdSupplier) {
        this.httpClient = httpClient;
        this.optionalResponseHandlerProvider = optionalResponseHandlerProvider;
        this.accessTokenSupplier = accessTokenSupplier;
        this.sessionAccountIdSupplier = sessionAccountIdSupplier;
    }

    static DefaultStatisticResource newInstance(final CloseableHttpClient httpClient,
                                                final OptionalResponseHandlerProvider optionalResponseHandlerProvider,
                                                final Supplier<String> sessionTokenSupplier,
                                                final Supplier<String> sessionAccountIdSupplier) {
        return new DefaultStatisticResource(
            httpClient,
            optionalResponseHandlerProvider,
            sessionTokenSupplier,
            sessionAccountIdSupplier
        );
    }

    private Optional<FilterableStatistic> findAllByAccountIdForWindow(final String accountId,
                                                                      final String window) throws IOException {
        return httpClient.execute(
            RequestBuilder.get(String.format(
                "%s/%s/bulk/window/%s",
                "https://fortnite-public-service-prod11.ol.epicgames.com/fortnite/api/stats/accountId",
                accountId,
                window
            ))
                .setHeader(AUTHORIZATION, "bearer " + accessTokenSupplier.get())
                .build(),
            optionalResponseHandlerProvider.forClass(RawStatistic[].class)
        )
            .map(Set::of)
            .map(DefaultFilterableStatistic::new);
    }

    @Override
    public Optional<FilterableStatistic> findAllByAccountIdForAllTime(final String accountId) throws IOException {
        Objects.requireNonNull(accountId, "accountId cannot be null");
        return findAllByAccountIdForWindow(accountId, "alltime");
    }

    @Override
    public Optional<FilterableStatistic> findAllBySessionAccountIdForAllTime() throws IOException {
        return findAllByAccountIdForAllTime(sessionAccountIdSupplier.get());
    }

    @Override
    public Optional<FilterableStatistic> findAllByAccountIdForCurrentSeason(final String accountId) throws IOException {
        Objects.requireNonNull(accountId, "accountId cannot be null");
        return findAllByAccountIdForWindow(accountId, "weekly");
    }

    @Override
    public Optional<FilterableStatistic> findAllBySessionAccountIdForCurrentSeason() throws IOException {
        return findAllByAccountIdForCurrentSeason(sessionAccountIdSupplier.get());
    }
}
