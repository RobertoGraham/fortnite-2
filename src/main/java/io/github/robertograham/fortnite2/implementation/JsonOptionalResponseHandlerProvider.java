package io.github.robertograham.fortnite2.implementation;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.json.bind.adapter.JsonbAdapter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
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

    JsonOptionalResponseHandlerProvider(final JsonbAdapter... jsonbAdapters) {
        stringOptionalHandler = responseHandlerFromHttpEntityToOptionalResultMapper(
            httpEntity -> Optional.ofNullable(EntityUtils.toString(httpEntity))
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
            httpEntity -> {
                try (final InputStream inputStream = httpEntity.getContent()) {
                    return Optional.ofNullable(inputStream)
                        .map(nonNullInputStream ->
                            jsonb.fromJson(nonNullInputStream, tClass)
                        );
                }
            }
        );
    }

    private <T> ResponseHandler<Optional<T>> responseHandlerFromHttpEntityToOptionalResultMapper(
        final HttpEntityToOptionalResultMapper<T> httpEntityToOptionalResultMapper
    ) {
        return response -> {
            final StatusLine statusLine = response.getStatusLine();
            final int statusCode = statusLine.getStatusCode();
            final HttpEntity httpEntity = response.getEntity();
            if (statusCode >= HttpStatus.SC_OK && statusCode < HttpStatus.SC_MULTIPLE_CHOICES)
                return httpEntity == null ?
                    Optional.empty()
                    : httpEntityToOptionalResultMapper.mapHttpEntityToOptionalResult(httpEntity);
            EntityUtils.consumeQuietly(httpEntity);
            throw new HttpResponseException(
                statusLine.getStatusCode(),
                String.format(
                    "[%d] %s",
                    statusLine.getStatusCode(),
                    Arrays.stream(response.getAllHeaders())
                        .filter(header -> "X-Epic-Error-Name".equals(header.getName()))
                        .findFirst()
                        .map(Header::getValue)
                        .filter(epicErrorName -> !epicErrorName.trim().isEmpty())
                        .orElseGet(statusLine::getReasonPhrase)
                )
            );
        };
    }

    private interface HttpEntityToOptionalResultMapper<T> {

        Optional<T> mapHttpEntityToOptionalResult(final HttpEntity httpEntity) throws IOException;
    }
}
