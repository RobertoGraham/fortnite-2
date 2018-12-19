package io.github.robertograham.fortniteclienttwo.implementation;

import io.github.robertograham.fortniteclienttwo.resource.Account;
import org.apache.http.client.HttpClient;

import java.util.function.Supplier;

final class DefaultAccount implements Account {

    private final HttpClient httpClient;
    private final Supplier<String> accessTokenSupplier;
    private final Runnable refreshSessionTokenRunnable;

    private DefaultAccount(HttpClient httpClient, Supplier<String> accessTokenSupplier, Runnable refreshSessionTokenRunnable) {
        this.httpClient = httpClient;
        this.accessTokenSupplier = accessTokenSupplier;
        this.refreshSessionTokenRunnable = refreshSessionTokenRunnable;
    }

    static DefaultAccount newInstance(HttpClient httpClient,
                                      Supplier<String> accessTokenSupplier,
                                      Runnable refreshSessionTokenRunnable) {
        return new DefaultAccount(httpClient, accessTokenSupplier, refreshSessionTokenRunnable);
    }
}
