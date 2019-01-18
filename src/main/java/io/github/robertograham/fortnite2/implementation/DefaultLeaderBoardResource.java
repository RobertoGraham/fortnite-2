package io.github.robertograham.fortnite2.implementation;

import io.github.robertograham.fortnite2.domain.LeaderBoardEntry;
import io.github.robertograham.fortnite2.domain.enumeration.PartyType;
import io.github.robertograham.fortnite2.domain.enumeration.Platform;
import io.github.robertograham.fortnite2.resource.LeaderBoardResource;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;

import javax.json.Json;
import java.io.IOException;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;

final class DefaultLeaderBoardResource implements LeaderBoardResource {

    private static final NameValuePair OWNER_TYPE_PARAMETER = new BasicNameValuePair("ownertype", "1");
    private static final NameValuePair PAGE_NUMBER_PARAMETER = new BasicNameValuePair("pageNumber", "0");
    private final CloseableHttpClient httpClient;
    private final OptionalResponseHandlerProvider optionalResponseHandlerProvider;
    private final Supplier<String> accessTokenSupplier;
    private final Supplier<String> inAppIdSupplier;

    private DefaultLeaderBoardResource(final CloseableHttpClient httpClient,
                                       final OptionalResponseHandlerProvider optionalResponseHandlerProvider,
                                       final Supplier<String> accessTokenSupplier,
                                       final Supplier<String> inAppIdSupplier) {
        this.httpClient = httpClient;
        this.optionalResponseHandlerProvider = optionalResponseHandlerProvider;
        this.accessTokenSupplier = accessTokenSupplier;
        this.inAppIdSupplier = inAppIdSupplier;
    }

    static DefaultLeaderBoardResource newInstance(final CloseableHttpClient httpClient,
                                                  final OptionalResponseHandlerProvider optionalResponseHandlerProvider,
                                                  final Supplier<String> sessionTokenSupplier,
                                                  final Supplier<String> inAppIdSupplier) {
        return new DefaultLeaderBoardResource(
            httpClient,
            optionalResponseHandlerProvider,
            sessionTokenSupplier,
            inAppIdSupplier
        );
    }

    private Optional<List<String>> cohortAccounts(final Platform platform,
                                                  final PartyType partyType) throws IOException {
        return httpClient.execute(
            RequestBuilder.get("https://fortnite-public-service-prod11.ol.epicgames.com/fortnite/api/game/v2/leaderboards/cohort/" + inAppIdSupplier.get())
                .addParameter("playlist", String.format("%s_m0_%s", platform.code(), partyType.code()))
                .setHeader(AUTHORIZATION, "bearer " + accessTokenSupplier.get())
                .build(),
            optionalResponseHandlerProvider.forClass(Cohort.class)
        )
            .map(Cohort::cohortAccounts);
    }

    @Override
    public Optional<List<LeaderBoardEntry>> findHighestWinnersByPlatformAndByPartyTypeForCurrentSeason(final Platform platform,
                                                                                                       final PartyType partyType,
                                                                                                       final int maxEntries) throws IOException {
        Objects.requireNonNull(platform, "platform cannot be null");
        Objects.requireNonNull(partyType, "partyType cannot be null");
        if (maxEntries < 0 || maxEntries > 1000)
            throw new IllegalArgumentException("maxEntries cannot be less than 0 or greater than 1000");
        return httpClient.execute(
            RequestBuilder.post(String.format(
                "https://fortnite-public-service-prod11.ol.epicgames.com/fortnite/api/leaderboards/type/global/stat/br_placetop1_%s_m0_%s/window/weekly",
                platform.code(),
                partyType.code()
            ))
                .addParameter(OWNER_TYPE_PARAMETER)
                .addParameter(PAGE_NUMBER_PARAMETER)
                .addParameter("itemsPerPage", String.valueOf(maxEntries))
                .setHeader(AUTHORIZATION, "bearer " + accessTokenSupplier.get())
                .setEntity(EntityBuilder.create()
                    .setContentType(APPLICATION_JSON)
                    .setText(Json.createArrayBuilder(cohortAccounts(platform, partyType)
                        .orElseGet(ArrayList::new))
                        .build()
                        .toString())
                    .build())
                .build(),
            optionalResponseHandlerProvider.forClass(RawLeaderBoard.class)
        )
            .map(RawLeaderBoard::leaderBoardEntries)
            .map(leaderBoardEntries ->
                leaderBoardEntries.stream()
                    .sorted(
                        Comparator.comparingLong(LeaderBoardEntry::value)
                            .reversed()
                    )
                    .collect(Collectors.toList())
            );
    }
}
