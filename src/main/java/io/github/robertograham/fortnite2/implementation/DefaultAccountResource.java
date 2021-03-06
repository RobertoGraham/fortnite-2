package io.github.robertograham.fortnite2.implementation;

import io.github.robertograham.fortnite2.domain.Account;
import io.github.robertograham.fortnite2.resource.AccountResource;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.apache.http.HttpHeaders.AUTHORIZATION;

final class DefaultAccountResource implements AccountResource {

    private static final int MAX_ID_COUNT_PER_ACCOUNTS_REQUEST = 100;
    private final CloseableHttpClient httpClient;
    private final OptionalResponseHandlerProvider optionalResponseHandlerProvider;
    private final Supplier<String> accessTokenSupplier;
    private final Supplier<String> sessionAccountIdSupplier;

    private DefaultAccountResource(final CloseableHttpClient httpClient,
                                   final OptionalResponseHandlerProvider optionalResponseHandlerProvider,
                                   final Supplier<String> accessTokenSupplier,
                                   final Supplier<String> sessionAccountIdSupplier) {
        this.httpClient = httpClient;
        this.optionalResponseHandlerProvider = optionalResponseHandlerProvider;
        this.accessTokenSupplier = accessTokenSupplier;
        this.sessionAccountIdSupplier = sessionAccountIdSupplier;
    }

    static DefaultAccountResource newInstance(final CloseableHttpClient httpClient,
                                              final OptionalResponseHandlerProvider optionalResponseHandlerProvider,
                                              final Supplier<String> sessionTokenSupplier,
                                              final Supplier<String> sessionAccountIdSupplier) {
        return new DefaultAccountResource(
            httpClient,
            optionalResponseHandlerProvider,
            sessionTokenSupplier,
            sessionAccountIdSupplier
        );
    }

    @Override
    public Optional<Account> findOneByDisplayName(final String displayName) throws IOException {
        Objects.requireNonNull(displayName, "displayName cannot be null");
        try {
            return httpClient.execute(
                RequestBuilder.get(new URIBuilder()
                    .setScheme("https")
                    .setHost("account-public-service-prod03.ol.epicgames.com")
                    .setPath(String.format(
                        "/%s/%s",
                        "account/api/public/account/displayName",
                        displayName
                    ))
                    .build())
                    .setHeader(AUTHORIZATION, "bearer " + accessTokenSupplier.get())
                    .build(),
                optionalResponseHandlerProvider.forClass(DefaultAccount.class)
            )
                .map(Function.identity());
        } catch (final URISyntaxException exception) {
            throw new IOException("Bad URI", exception);
        }
    }

    @Override
    public Optional<Account> findOneBySessionAccountId() throws IOException {
        return findAllByAccountIds(sessionAccountIdSupplier.get())
            .flatMap((final var accountSet) ->
                accountSet.stream()
                    .findFirst()
            );
    }

    @Override
    public Optional<Set<Account>> findAllByAccountIds(final String... accountIds) throws IOException {
        Objects.requireNonNull(accountIds, "accountIds cannot be null");
        if (Arrays.stream(accountIds)
            .anyMatch(Objects::isNull))
            throw new NullPointerException("accountIds cannot contain null value");
        final var accountIdPartitionSetSet = IntStream.range(0, accountIds.length)
            .boxed()
            .collect(Collectors.groupingBy((final var integer) ->
                integer / MAX_ID_COUNT_PER_ACCOUNTS_REQUEST
            ))
            .values()
            .stream()
            .map((final var integerList) ->
                integerList.stream()
                    .map((final var integer) -> accountIds[integer])
                    .collect(Collectors.toSet())
            )
            .collect(Collectors.toSet());
        final var optionalAccountSetSet = new HashSet<Optional<Set<Account>>>();
        for (final var accountIdPartitionSet : accountIdPartitionSetSet)
            optionalAccountSetSet.add(findAllByAccountIds(accountIdPartitionSet));
        return optionalAccountSetSet.stream()
            .reduce((final var optionalAccountSet,
                     final var optionalAccountSetAccumulator) ->
                optionalAccountSetAccumulator.map((final var accountSet) -> {
                    accountSet.addAll(optionalAccountSet.orElseGet(HashSet::new));
                    return accountSet;
                })
            )
            .orElseGet(Optional::empty);
    }

    private Optional<Set<Account>> findAllByAccountIds(final Set<String> accountIds) throws IOException {
        return httpClient.execute(
            RequestBuilder.get("https://account-public-service-prod03.ol.epicgames.com/account/api/public/account")
                .addParameters(accountIds.stream()
                    .map((final var accountIdString) -> new BasicNameValuePair("accountId", accountIdString))
                    .toArray(BasicNameValuePair[]::new))
                .setHeader(AUTHORIZATION, "bearer " + accessTokenSupplier.get())
                .build(),
            optionalResponseHandlerProvider.forClass(DefaultAccount[].class)
        )
            .map(Set::of);
    }
}
