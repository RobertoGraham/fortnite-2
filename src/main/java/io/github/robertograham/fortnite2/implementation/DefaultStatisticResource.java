package io.github.robertograham.fortnite2.implementation;

import io.github.robertograham.fortnite2.domain.Account;
import io.github.robertograham.fortnite2.domain.FilterableStatistic;
import io.github.robertograham.fortnite2.resource.StatisticResource;
import org.apache.http.HttpHeaders;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

final class DefaultStatisticResource implements StatisticResource {

    private static final URI BATTLE_ROYALE_STATS_URI = URI.create("https://fortnite-public-service-prod11.ol.epicgames.com/fortnite/api/stats/accountId");
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

    private Optional<FilterableStatistic> findAllByAccountIdForWindow(String accountId, String window) throws IOException {
        final HttpGet httpGet = new HttpGet(
                String.format(
                        "%s/%s/bulk/window/%s",
                        BATTLE_ROYALE_STATS_URI,
                        accountId,
                        window
                )
        );
        httpGet.setHeader(HttpHeaders.AUTHORIZATION, String.format("bearer %s", accessTokenSupplier.get()));
        return httpClient.execute(
                httpGet,
                optionalResponseHandlerProvider.forClass(RawStatistic[].class)
        )
                .map(rawStatistics -> new HashSet<>(Arrays.asList(rawStatistics)))
                .map(DefaultFilterableStatistic::new);
    }

    @Override
    public Optional<FilterableStatistic> findAllByAccountIdForAllTime(String accountId) throws IOException {
        Objects.requireNonNull(accountId, "accountId cannot be null");
        return findAllByAccountIdForWindow(accountId, "alltime");
    }

    @Override
    public Optional<FilterableStatistic> findAllByAccountForAllTime(Account account) throws IOException {
        Objects.requireNonNull(account, "account cannot be null");
        return findAllByAccountIdForAllTime(account.accountId());
    }

    @Override
    public Optional<FilterableStatistic> findAllByAccountIdForCurrentSeason(String accountId) throws IOException {
        Objects.requireNonNull(accountId, "accountId cannot be null");
        return findAllByAccountIdForWindow(accountId, "weekly");
    }

    @Override
    public Optional<FilterableStatistic> findAllByAccountForCurrentSeason(Account account) throws IOException {
        Objects.requireNonNull(account, "account cannot be null");
        return findAllByAccountIdForCurrentSeason(account.accountId());
    }
}
