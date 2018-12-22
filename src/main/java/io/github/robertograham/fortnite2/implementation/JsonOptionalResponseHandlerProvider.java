package io.github.robertograham.fortnite2.implementation;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.json.bind.adapter.JsonbAdapter;
import java.io.IOException;
import java.util.Optional;

enum JsonOptionalResponseHandlerProvider implements OptionalResponseHandlerProvider {

    INSTANCE(
            Token.Adapter.INSTANCE,
            Exchange.Adapter.INSTANCE,
            DefaultAccount.Adapter.INSTANCE,
            RawStatistic.Adapter.INSTANCE
    );

    private final ResponseHandler<Optional<String>> stringOptionalHandler;
    private final Jsonb jsonb;

    JsonOptionalResponseHandlerProvider(JsonbAdapter... jsonbAdapters) {
        stringOptionalHandler = response -> {
            final Optional<String> entityAsString = Optional.ofNullable(response.getEntity())
                    .map(httpEntity -> {
                        try {
                            return EntityUtils.toString(httpEntity);
                        } catch (IOException ignored) {
                            return null;
                        }
                    });
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode >= 200 && statusCode < 300)
                return entityAsString;
            else
                throw new ClientProtocolException(String.format("Unexpected response [%d]: %s", statusCode, entityAsString.orElse("")));
        };
        jsonb = JsonbBuilder.create(
                new JsonbConfig()
                        .withAdapters(jsonbAdapters)
        );
    }

    @Override
    public ResponseHandler<Optional<String>> forString() {
        return stringOptionalHandler;
    }

    @Override
    public <T> ResponseHandler<Optional<T>> forClass(Class<T> clazz) {
        return response ->
                stringOptionalHandler.handleResponse(response)
                        .map(string -> jsonb.fromJson(string, clazz));
    }
}
