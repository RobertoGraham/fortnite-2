package io.github.robertograham.fortniteclienttwo.implementation;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import java.io.IOException;
import java.util.Optional;

enum ResponseHandlerProvider {

    INSTANCE;

    static final ResponseHandler<Optional<String>> STRING_OPTIONAL_RESPONSE_HANDLER = response -> {
        final Optional<String> entityAsStringOptional = Optional.ofNullable(response.getEntity())
                .map(nonNullEntity -> {
                    try {
                        return EntityUtils.toString(nonNullEntity);
                    } catch (IOException e) {
                        return null;
                    }
                });
        final int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode >= 200 && statusCode < 300)
            return entityAsStringOptional;
        else
            throw new ClientProtocolException(String.format("Unexpected response [%d]: [%s]", statusCode, entityAsStringOptional.orElse("")));
    };
    private final Jsonb jsonb;

    ResponseHandlerProvider() {
        jsonb = JsonbBuilder.create(
                new JsonbConfig()
                        .withAdapters(
                                Token.Adapter.INSTANCE,
                                Exchange.Adapter.INSTANCE,
                                DefaultAccount.Adapter.INSTANCE
                        )
        );
    }

    <T> ResponseHandler<Optional<T>> responseHandlerForClass(Class<T> clazz) {
        return response ->
                STRING_OPTIONAL_RESPONSE_HANDLER.handleResponse(response)
                        .map(string ->
                                jsonb.fromJson(string, clazz)
                        );
    }
}
