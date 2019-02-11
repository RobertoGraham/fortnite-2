package io.github.robertograham.fortnite2.implementation;

import org.apache.http.Header;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;

import javax.json.JsonObject;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * Gives insight into what caused an HTTP error response
 */
public final class EpicGamesErrorException extends HttpResponseException {

    private final String type;
    private final JsonObject jsonObject;

    EpicGamesErrorException(final int statusCode, final JsonObject jsonObject) {
        super(statusCode, jsonObject.toString());
        this.type = jsonObject.getString("errorCode", "");
        this.jsonObject = jsonObject;
    }

    EpicGamesErrorException(final StatusLine statusLine, final Header[] headers) {
        super(statusLine.getStatusCode(), statusLine.getReasonPhrase());
        this.type = Optional.ofNullable(headers)
            .flatMap((final var nonNullHeaders) -> Arrays.stream(nonNullHeaders)
                .filter(Objects::nonNull)
                .filter((final var header) -> "X-Epic-Error-Name".equals(header.getName()))
                .findFirst()
                .map(Header::getValue)
                .filter((final var epicErrorNameString) -> !epicErrorNameString.isBlank()))
            .orElse("");
        jsonObject = null;
    }

    /**
     * @return {@code ""} if a type could not be determined, otherwise it's an Epic Games error code
     */
    public String type() {
        return type;
    }

    /**
     * @return an instance of {@link Optional} that's non-empty if the
     * causing HTTP response had a JSON error body
     */
    public Optional<JsonObject> jsonObject() {
        return Optional.ofNullable(jsonObject);
    }
}
