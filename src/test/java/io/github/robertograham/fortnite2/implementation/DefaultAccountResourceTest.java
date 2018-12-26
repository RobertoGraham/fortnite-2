package io.github.robertograham.fortnite2.implementation;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;

import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultAccountResourceTest {

    @Mock
    private CloseableHttpClient mockHttpClient;
    @Mock
    private OptionalResponseHandlerProvider mockOptionalResponseHandlerProvider;
    @Mock
    private Supplier<String> mockAccessTokenSupplier;
    @InjectMocks
    private DefaultAccountResource accountResource;
    @Captor
    private ArgumentCaptor<HttpUriRequest> requestArgumentCaptor;
    @Captor
    private ArgumentCaptor<ResponseHandler<Optional<DefaultAccount>>> defaultAccountOptionalResponseHandlerArgumentCaptor;

    @Test
    @DisplayName("findOneByDisplayName throws NPE")
    void findOneByDisplayNameThrowsNPE() {
        assertThrows(
                NullPointerException.class,
                () -> accountResource.findOneByDisplayName(null),
                "displayName cannot be null"
        );
    }

    @Test
    @DisplayName("findOneByDisplayName executes correct request")
    void findOneByDisplayNameUsesCorrectRequest() throws IOException {
        ResponseHandler<Optional<DefaultAccount>> responseHandler = response -> Optional.empty();
        when(mockOptionalResponseHandlerProvider.forClass(DefaultAccount.class))
                .thenReturn(responseHandler);
        when(mockHttpClient.execute(any(HttpUriRequest.class), any(ResponseHandler.class)))
                .thenReturn(responseHandler.handleResponse(null));
        when(mockAccessTokenSupplier.get())
                .thenReturn("accessToken");
        accountResource.findOneByDisplayName("displayName");
        verify(mockHttpClient, times(1))
                .execute(requestArgumentCaptor.capture(), defaultAccountOptionalResponseHandlerArgumentCaptor.capture());
        assertEquals(responseHandler, defaultAccountOptionalResponseHandlerArgumentCaptor.getValue());
        HttpUriRequest actualRequest = requestArgumentCaptor.getValue();
        assertEquals(HttpGet.METHOD_NAME, actualRequest.getMethod());
        assertEquals(
                URI.create("https://persona-public-service-prod06.ol.epicgames.com/persona/api/public/account/lookup?q=displayName"),
                actualRequest.getURI()
        );
        assertTrue(Arrays.stream(actualRequest.getAllHeaders())
                .anyMatch(header ->
                        AUTHORIZATION.equals(header.getName())
                                && "bearer accessToken".equals(header.getValue())
                ));
    }
}
