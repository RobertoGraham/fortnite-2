package io.github.robertograham.fortnite2.implementation;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import javax.json.Json;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.json.bind.adapter.JsonbAdapter;
import javax.json.stream.JsonParsingException;
import java.io.IOException;
import java.util.Optional;

enum JsonOptionalResponseHandlerProvider implements OptionalResponseHandlerProvider {

    INSTANCE(
        DefaultToken.Adapter.INSTANCE,
        Exchange.Adapter.INSTANCE,
        DefaultAccount.Adapter.INSTANCE,
        RawStatistic.Adapter.INSTANCE,
        Cohort.Adapter.INSTANCE,
        RawLeaderBoard.Adapter.INSTANCE,
        DefaultFriendRequest.Adapter.INSTANCE,
        Eula.Adapter.INSTANCE
    );

    private final ResponseHandler<Optional<String>> stringOptionalHandler;
    private final Jsonb jsonb;

    JsonOptionalResponseHandlerProvider(final JsonbAdapter... jsonbAdapters) {
        stringOptionalHandler = responseHandlerFromHttpEntityToOptionalResultMapper(
            (final var httpEntity) -> Optional.ofNullable(EntityUtils.toString(httpEntity))
        );
        jsonb = JsonbBuilder.create(new JsonbConfig()
            .withAdapters(jsonbAdapters));
    }

    @Override
    public ResponseHandler<Optional<String>> forString() {
        return stringOptionalHandler;
    }

    @Override
    public <T> ResponseHandler<Optional<T>> forClass(final Class<T> tClass) {
        return responseHandlerFromHttpEntityToOptionalResultMapper(
            (final var httpEntity) -> {
                try (final var inputStream = httpEntity.getContent()) {
                    return Optional.ofNullable(inputStream)
                        .map(nonNullInputStream -> jsonb.fromJson(nonNullInputStream, tClass));
                }
            }
        );
    }

    private <T> ResponseHandler<Optional<T>> responseHandlerFromHttpEntityToOptionalResultMapper(
        final HttpEntityToOptionalResultMapper<T> httpEntityToOptionalResultMapper
    ) {
        return response -> {
            final var statusLine = response.getStatusLine();
            final var statusCodeInt = statusLine.getStatusCode();
            final var httpEntity = response.getEntity();
            if (statusCodeInt >= HttpStatus.SC_OK && statusCodeInt < HttpStatus.SC_MULTIPLE_CHOICES)
                return httpEntity == null ?
                    Optional.empty()
                    : httpEntityToOptionalResultMapper.mapHttpEntityToOptionalResult(httpEntity);
            final var inputStream = httpEntity != null ?
                httpEntity.getContent()
                : null;
            if (inputStream == null)
                throw new EpicGamesErrorException(statusLine, response.getAllHeaders());
            try (inputStream; final var jsonReader = Json.createReader(inputStream)) {
                throw new EpicGamesErrorException(statusCodeInt, jsonReader.readObject());
            } catch (final JsonParsingException exception) {
                throw new EpicGamesErrorException(statusLine, response.getAllHeaders());
            }
        };
    }

    private interface HttpEntityToOptionalResultMapper<T> {

        Optional<T> mapHttpEntityToOptionalResult(final HttpEntity httpEntity) throws IOException;
    }
}