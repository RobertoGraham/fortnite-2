package io.github.robertograham.fortnite2.implementation;

import io.github.robertograham.fortnite2.domain.Account;
import io.github.robertograham.fortnite2.resource.AccountResource;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
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

    private DefaultAccountResource(CloseableHttpClient httpClient,
                                   OptionalResponseHandlerProvider optionalResponseHandlerProvider,
                                   Supplier<String> accessTokenSupplier,
                                   Supplier<String> sessionAccountIdSupplier) {
        this.httpClient = httpClient;
        this.optionalResponseHandlerProvider = optionalResponseHandlerProvider;
        this.accessTokenSupplier = accessTokenSupplier;
        this.sessionAccountIdSupplier = sessionAccountIdSupplier;
    }

    static DefaultAccountResource newInstance(CloseableHttpClient httpClient,
                                              OptionalResponseHandlerProvider optionalResponseHandlerProvider,
                                              Supplier<String> sessionTokenSupplier,
                                              Supplier<String> sessionAccountIdSupplier) {
        return new DefaultAccountResource(
                httpClient,
                optionalResponseHandlerProvider,
                sessionTokenSupplier,
                sessionAccountIdSupplier
        );
    }

    @Override
    public Optional<Account> findOneByDisplayName(String displayName) throws IOException {
        Objects.requireNonNull(displayName, "displayName cannot be null");
        return httpClient.execute(
                RequestBuilder.get("https://persona-public-service-prod06.ol.epicgames.com/persona/api/public/account/lookup")
                        .addParameter("q", displayName)
                        .setHeader(AUTHORIZATION, "bearer " + accessTokenSupplier.get())
                        .build(),
                optionalResponseHandlerProvider.forClass(DefaultAccount.class)
        )
                .map(Function.identity());
    }

    @Override
    public Optional<Account> findOneBySessionAccountId() throws IOException {
        return findAllByAccountIds(sessionAccountIdSupplier.get())
                .flatMap(accounts ->
                        accounts.stream()
                                .findFirst()
                );
    }

    @Override
    public Optional<Set<Account>> findAllByAccountIds(String... accountIds) throws IOException {
        Objects.requireNonNull(accountIds, "accountIds cannot be null");
        if (Arrays.stream(accountIds)
                .anyMatch(Objects::isNull))
            throw new NullPointerException("accountIds cannot contain null value");
        final Set<Set<String>> accountIdsPartitioned = IntStream.range(0, accountIds.length)
                .boxed()
                .collect(
                        Collectors.groupingBy(index ->
                                index / MAX_ID_COUNT_PER_ACCOUNTS_REQUEST
                        )
                )
                .values()
                .stream()
                .map(indices ->
                        indices.stream()
                                .map(index -> accountIds[index])
                                .collect(Collectors.toSet())
                )
                .collect(Collectors.toSet());
        final Set<Optional<Set<Account>>> optionalAccountSetSet = new HashSet<>();
        for (final Set<String> accountIdPartition : accountIdsPartitioned)
            optionalAccountSetSet.add(findAllByAccountIds(accountIdPartition));
        return optionalAccountSetSet.stream()
                .reduce((optionalAccountSetToAdd, optionalAccountSetAccumulator) ->
                        optionalAccountSetAccumulator.map(accountSet -> {
                            accountSet.addAll(
                                    optionalAccountSetToAdd.orElseGet(HashSet::new)
                            );
                            return accountSet;
                        })
                )
                .orElseGet(Optional::empty);
    }

    private Optional<Set<Account>> findAllByAccountIds(Set<String> accountIds) throws IOException {
        return httpClient.execute(
                RequestBuilder.get("https://account-public-service-prod03.ol.epicgames.com/account/api/public/account")
                        .addParameters(
                                accountIds.stream()
                                        .map(accountId -> new BasicNameValuePair("accountId", accountId))
                                        .toArray(BasicNameValuePair[]::new)
                        )
                        .setHeader(AUTHORIZATION, "bearer " + accessTokenSupplier.get())
                        .build(),
                optionalResponseHandlerProvider.forClass(DefaultAccount[].class)
        )
                .map(accounts -> new HashSet<>(Arrays.asList(accounts)));
    }
}
