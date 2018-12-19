package io.github.robertograham.fortniteclienttwo.implementation;

import io.github.robertograham.fortniteclienttwo.domain.Account;
import io.github.robertograham.fortniteclienttwo.resource.AccountResource;
import org.apache.http.HttpHeaders;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

final class DefaultAccountResource implements AccountResource {

    private static final URI LOOKUP_URI = URI.create("https://persona-public-service-prod06.ol.epicgames.com/persona/api/public/account/lookup?q=");
    private final HttpClient httpClient;
    private final Supplier<String> accessTokenSupplier;

    private DefaultAccountResource(HttpClient httpClient,
                                   Supplier<String> accessTokenSupplier) {
        this.httpClient = httpClient;
        this.accessTokenSupplier = accessTokenSupplier;
    }

    static DefaultAccountResource newInstance(HttpClient httpClient,
                                              Supplier<String> sessionTokenSupplier) {
        return new DefaultAccountResource(httpClient, sessionTokenSupplier);
    }

    @Override
    public Optional<Account> accountFromDisplayName(String displayName) throws IOException {
        final HttpGet httpGet = new HttpGet(String.format("%s%s", LOOKUP_URI, displayName));
        httpGet.setHeader(HttpHeaders.AUTHORIZATION, String.format("bearer %s", accessTokenSupplier.get()));
        return httpClient.execute(
                httpGet,
                ResponseHandlerProvider.INSTANCE.responseHandlerForClass(DefaultAccount.class)
        )
                .map(Function.identity());
    }
}
