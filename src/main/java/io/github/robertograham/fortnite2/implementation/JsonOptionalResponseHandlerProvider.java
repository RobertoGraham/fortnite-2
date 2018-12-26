package io.github.robertograham.fortnite2.implementation;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.json.bind.adapter.JsonbAdapter;
import java.util.Optional;

enum JsonOptionalResponseHandlerProvider implements OptionalResponseHandlerProvider {

    INSTANCE(
            Token.Adapter.INSTANCE,
            Exchange.Adapter.INSTANCE,
            DefaultAccount.Adapter.INSTANCE,
            RawStatistic.Adapter.INSTANCE,
            Cohort.Adapter.INSTANCE,
            RawLeaderBoard.Adapter.INSTANCE
    );

    private final ResponseHandler<Optional<String>> stringOptionalHandler;
    private final Jsonb jsonb;

    JsonOptionalResponseHandlerProvider(JsonbAdapter... jsonbAdapters) {
        stringOptionalHandler = response -> {
            final HttpEntity entity = response.getEntity();
            final Optional<String> entityAsStringOptional = entity == null ?
                    Optional.empty()
                    : Optional.ofNullable(EntityUtils.toString(entity));
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode >= HttpStatus.SC_OK
                    && statusCode < HttpStatus.SC_MULTIPLE_CHOICES)
                return entityAsStringOptional;
            else
                throw new ClientProtocolException(String.format("Unexpected response [%d]: %s", statusCode, entityAsStringOptional.orElse("")));
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
