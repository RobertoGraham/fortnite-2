package io.github.robertograham.fortniteclienttwo.implementation;

import io.github.robertograham.fortniteclienttwo.resource.Account;
import org.apache.http.client.HttpClient;

import java.util.function.Supplier;

final class DefaultAccount implements Account {

    private final HttpClient httpClient;
    private final Supplier<String> accessTokenSupplier;

    private DefaultAccount(HttpClient httpClient,
                           Supplier<String> accessTokenSupplier) {
        this.httpClient = httpClient;
        this.accessTokenSupplier = accessTokenSupplier;
    }

    static DefaultAccount newInstance(HttpClient httpClient,
                                      Supplier<String> sessionTokenSupplier) {
        return new DefaultAccount(httpClient, sessionTokenSupplier);
    }
}
