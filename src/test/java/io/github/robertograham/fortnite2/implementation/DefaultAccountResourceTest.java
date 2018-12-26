package io.github.robertograham.fortnite2.implementation;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

class DefaultAccountResourceTest {

    private final DefaultAccountResource defaultAccountResource = DefaultAccountResource.newInstance(
            new NoOpCloseableHttpClient(),
            new NoOpOptionalResponseHandlerProvider(),
            () -> null
    );

    @Test
    @DisplayName("findOneByDisplayName throws NPE")
    void findOneByDisplayNameThrowsNPE() {
        assertThrows(
                NullPointerException.class,
                () -> defaultAccountResource.findOneByDisplayName(null),
                "displayName cannot be null"
        );
    }

    private static final class NoOpOptionalResponseHandlerProvider implements OptionalResponseHandlerProvider {

        @Override
        public ResponseHandler<Optional<String>> forString() {
            return response -> Optional.empty();
        }

        @Override
        public <T> ResponseHandler<Optional<T>> forClass(Class<T> tClass) {
            return response -> Optional.empty();
        }
    }

    private static final class NoOpCloseableHttpClient extends CloseableHttpClient {

        @Override
        protected CloseableHttpResponse doExecute(HttpHost target, HttpRequest request, HttpContext context) throws IOException {
            return null;
        }

        @Override
        public void close() {
        }

        @Override
        public HttpParams getParams() {
            return null;
        }

        @Override
        public ClientConnectionManager getConnectionManager() {
            return null;
        }
    }
}
