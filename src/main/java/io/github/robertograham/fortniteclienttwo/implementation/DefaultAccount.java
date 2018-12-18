package io.github.robertograham.fortniteclienttwo.implementation;

import io.github.robertograham.fortniteclienttwo.resource.Account;
import org.apache.http.client.HttpClient;

final class DefaultAccount implements Account {

    private final HttpClient httpClient;

    private DefaultAccount(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    static DefaultAccount newInstance(HttpClient httpClient) {
        return new DefaultAccount(httpClient);
    }
}
