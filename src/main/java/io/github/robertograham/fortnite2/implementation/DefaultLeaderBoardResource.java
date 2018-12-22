package io.github.robertograham.fortnite2.implementation;

import io.github.robertograham.fortnite2.domain.LeaderBoardEntry;
import io.github.robertograham.fortnite2.domain.enumeration.PartyType;
import io.github.robertograham.fortnite2.domain.enumeration.Platform;
import io.github.robertograham.fortnite2.resource.LeaderBoardResource;
import org.apache.http.HttpHeaders;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import javax.json.Json;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

final class DefaultLeaderBoardResource implements LeaderBoardResource {

    private static final URI COHORT_URI = URI.create("https://fortnite-public-service-prod11.ol.epicgames.com/fortnite/api/game/v2/leaderboards/cohort");
    private static final URI LEADER_BOARD_URI = URI.create("https://fortnite-public-service-prod11.ol.epicgames.com/fortnite/api/leaderboards/type/global/stat");
    private final HttpClient httpClient;
    private final OptionalResponseHandlerProvider optionalResponseHandlerProvider;
    private final Supplier<String> accessTokenSupplier;
    private final Supplier<String> inAppIdSupplier;

    private DefaultLeaderBoardResource(HttpClient httpClient,
                                       OptionalResponseHandlerProvider optionalResponseHandlerProvider,
                                       Supplier<String> accessTokenSupplier,
                                       Supplier<String> inAppIdSupplier) {
        this.httpClient = httpClient;
        this.optionalResponseHandlerProvider = optionalResponseHandlerProvider;
        this.accessTokenSupplier = accessTokenSupplier;
        this.inAppIdSupplier = inAppIdSupplier;
    }

    static DefaultLeaderBoardResource newInstance(HttpClient httpClient,
                                                  OptionalResponseHandlerProvider optionalResponseHandlerProvider,
                                                  Supplier<String> sessionTokenSupplier,
                                                  Supplier<String> inAppIdSupplier) {
        return new DefaultLeaderBoardResource(
                httpClient,
                optionalResponseHandlerProvider,
                sessionTokenSupplier,
                inAppIdSupplier
        );
    }

    private Optional<Cohort> cohort(Platform platform, PartyType partyType) throws IOException {
        final HttpGet httpGet = new HttpGet(
                String.format(
                        "%s/%s?playlist=%s_m0_%s",
                        COHORT_URI,
                        inAppIdSupplier.get(),
                        platform.code(),
                        partyType.code()
                )
        );
        httpGet.setHeader(HttpHeaders.AUTHORIZATION, String.format("bearer %s", accessTokenSupplier.get()));
        return httpClient.execute(
                httpGet,
                optionalResponseHandlerProvider.forClass(Cohort.class)
        );
    }

    @Override
    public Optional<List<LeaderBoardEntry>> findHighestWinnersByPlatformAndByPartyTypeForCurrentSeason(Platform platform, PartyType partyType, int maxEntries) throws IOException {
        Objects.requireNonNull(platform, "platform cannot be null");
        Objects.requireNonNull(partyType, "partyType cannot be null");
        if (maxEntries < 0 || maxEntries > 1000)
            throw new IllegalArgumentException("maxEntries cannot be less than 0 or greater than 1000");
        final HttpPost httpPost = new HttpPost(
                String.format(
                        "%s/br_placetop1_%s_m0_%s/window/weekly?ownertype=1&pageNumber=0&itemsPerPage=%d",
                        LEADER_BOARD_URI,
                        platform.code(),
                        partyType.code(),
                        maxEntries
                )
        );
        httpPost.setHeader(HttpHeaders.AUTHORIZATION, String.format("bearer %s", accessTokenSupplier.get()));
        httpPost.setEntity(
                new StringEntity(
                        Json.createArrayBuilder(
                                cohort(platform, partyType)
                                        .map(Cohort::cohortAccounts)
                                        .orElseGet(ArrayList::new)
                        )
                                .build()
                                .toString(),
                        ContentType.APPLICATION_JSON
                )
        );
        return httpClient.execute(
                httpPost,
                optionalResponseHandlerProvider.forClass(RawLeaderBoard.class)
        )
                .map(RawLeaderBoard::leaderBoardEntries);
    }
}
