package io.github.robertograham.fortniteclienttwo.implementation;

import io.github.robertograham.fortniteclienttwo.domain.Account;
import io.github.robertograham.fortniteclienttwo.resource.AccountResource;
import org.apache.http.HttpHeaders;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

final class DefaultAccountResource implements AccountResource {

    private static final URI SINGLE_ACCOUNT_URI = URI.create("https://persona-public-service-prod06.ol.epicgames.com/persona/api/public/account/lookup?q=");
    private static final URI MULTIPLE_ACCOUNTS_URI = URI.create("https://account-public-service-prod03.ol.epicgames.com/account/api/public/account?");
    private final HttpClient httpClient;
    private final OptionalResponseHandlerProvider optionalResponseHandlerProvider;
    private final Supplier<String> accessTokenSupplier;

    private DefaultAccountResource(HttpClient httpClient,
                                   OptionalResponseHandlerProvider optionalResponseHandlerProvider,
                                   Supplier<String> accessTokenSupplier) {
        this.httpClient = httpClient;
        this.optionalResponseHandlerProvider = optionalResponseHandlerProvider;
        this.accessTokenSupplier = accessTokenSupplier;
    }

    static DefaultAccountResource newInstance(HttpClient httpClient,
                                              OptionalResponseHandlerProvider optionalResponseHandlerProvider,
                                              Supplier<String> sessionTokenSupplier) {
        return new DefaultAccountResource(
                httpClient,
                optionalResponseHandlerProvider,
                sessionTokenSupplier);
    }

    @Override
    public Optional<Account> findOneByDisplayName(String displayName) throws IOException {
        Objects.requireNonNull(displayName, "displayName cannot be null");
        final HttpGet httpGet = new HttpGet(String.format("%s%s", SINGLE_ACCOUNT_URI, displayName));
        httpGet.setHeader(HttpHeaders.AUTHORIZATION, String.format("bearer %s", accessTokenSupplier.get()));
        return httpClient.execute(
                httpGet,
                optionalResponseHandlerProvider.forClass(DefaultAccount.class)
        )
                .map(Function.identity());
    }

    @Override
    public Optional<Set<Account>> findAllByAccountIds(String... accountIds) throws IOException {
        Objects.requireNonNull(accountIds, "accountIds cannot be null");
        if (Arrays.stream(accountIds)
                .anyMatch(Objects::isNull))
            throw new NullPointerException("accountIds cannot contain null value");
        final HttpGet httpGet = new HttpGet(
                String.format(
                        "%s%s",
                        MULTIPLE_ACCOUNTS_URI,
                        Arrays.stream(accountIds)
                                .map("accountId="::concat)
                                .collect(Collectors.joining("&"))
                )
        );
        httpGet.setHeader(HttpHeaders.AUTHORIZATION, String.format("bearer %s", accessTokenSupplier.get()));
        return httpClient.execute(
                httpGet,
                optionalResponseHandlerProvider.forClass(DefaultAccount[].class)
        )
                .map(accounts -> new HashSet<>(Arrays.asList(accounts)));
    }
}
