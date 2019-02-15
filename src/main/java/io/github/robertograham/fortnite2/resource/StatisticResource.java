package io.github.robertograham.fortnite2.resource;

import io.github.robertograham.fortnite2.domain.Account;
import io.github.robertograham.fortnite2.domain.FilterableStatistic;
import io.github.robertograham.fortnite2.domain.FilterableStatisticV2;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * An object from which statistic related API endpoints can be called
 *
 * @since 1.0.0
 */
public interface StatisticResource {

    /**
     * @param accountId ID of the Epic Games account to fetch all time statistics
     *                  for
     * @return an {@link Optional} of {@link FilterableStatistic} that's non-empty
     * is the API response isn't empty
     * @throws IOException          if there's an unexpected HTTP status code (less than
     *                              200 or greater than 299) or if there's a problem reading the
     *                              API response
     * @throws NullPointerException if {@code accountId} is {@code null}
     * @since 1.0.0
     */
    Optional<FilterableStatistic> findAllByAccountIdForAllTime(final String accountId) throws IOException;

    /**
     * @param account Epic Games account to fetch all time statistics for
     * @return an {@link Optional} of {@link FilterableStatistic} that's non-empty
     * is the API response isn't empty
     * @throws IOException          if there's an unexpected HTTP status code (less than
     *                              200 or greater than 299) or if there's a problem reading the
     *                              API response
     * @throws NullPointerException if {@code account} is {@code null}
     * @since 1.0.0
     */
    @Deprecated
    default Optional<FilterableStatistic> findAllByAccountForAllTime(final Account account) throws IOException {
        Objects.requireNonNull(account, "account cannot be null");
        return findAllByAccountIdForAllTime(account.accountId());
    }

    /**
     * @return an {@link Optional} of {@link FilterableStatistic} that's non-empty
     * is the API response isn't empty
     * @throws IOException if there's an unexpected HTTP status code (less than
     *                     200 or greater than 299) or if there's a problem reading the
     *                     API response
     * @since 1.2.0
     */
    @Deprecated
    Optional<FilterableStatistic> findAllBySessionAccountIdForAllTime() throws IOException;

    /**
     * @param accountId ID of the Epic Games account to fetch current season
     *                  statistics for
     * @return an {@link Optional} of {@link FilterableStatistic} that's non-empty
     * is the API response isn't empty
     * @throws IOException          if there's an unexpected HTTP status code (less than
     *                              200 or greater than 299) or if there's a problem reading the
     *                              API response
     * @throws NullPointerException if {@code accountId} is {@code null}
     * @since 1.0.0
     */
    @Deprecated
    Optional<FilterableStatistic> findAllByAccountIdForCurrentSeason(final String accountId) throws IOException;

    /**
     * @param account Epic Games account to fetch current season statistics for
     * @return an {@link Optional} of {@link FilterableStatistic} that's non-empty
     * is the API response isn't empty
     * @throws IOException          if there's an unexpected HTTP status code (less than
     *                              200 or greater than 299) or if there's a problem reading the
     *                              API response
     * @throws NullPointerException if {@code account} is {@code null}
     * @since 1.0.0
     */
    @Deprecated
    default Optional<FilterableStatistic> findAllByAccountForCurrentSeason(final Account account) throws IOException {
        Objects.requireNonNull(account, "account cannot be null");
        return findAllByAccountIdForCurrentSeason(account.accountId());
    }

    /**
     * @return an {@link Optional} of {@link FilterableStatistic} that's non-empty
     * is the API response isn't empty
     * @throws IOException if there's an unexpected HTTP status code (less than
     *                     200 or greater than 299) or if there's a problem reading the
     *                     API response
     * @since 1.2.0
     */
    @Deprecated
    Optional<FilterableStatistic> findAllBySessionAccountIdForCurrentSeason() throws IOException;

    /**
     * @param accountId ID of the Epic Games account to fetch the statistics for
     * @param startTime start of the time window
     * @param endTime   end of the time window
     * @return an {@link Optional} of {@link FilterableStatisticV2} that's non-empty
     * if the API response isn't empty
     * @throws IOException              if there's an unexpected HTTP status code (less than
     *                                  200 or greater than 299) or if there's a problem reading the
     *                                  API response
     * @throws NullPointerException     if {@code accountId} is {@code null}
     * @throws NullPointerException     if {@code startTime} is {@code null}
     * @throws NullPointerException     if {@code endTime} is {@code null}
     * @throws IllegalArgumentException if {@code startTime} is greater than or equal to {@code endTime}
     * @since 3.1.0
     */
    default Optional<FilterableStatisticV2> findAllByAccountId(final String accountId,
                                                               final ZonedDateTime startTime,
                                                               final ZonedDateTime endTime) throws IOException {
        return Optional.empty();
    }

    /**
     * @param account   the Epic Games account to fetch the statistics for
     * @param startTime start of the time window
     * @param endTime   end of the time window
     * @return an {@link Optional} of {@link FilterableStatisticV2} that's non-empty
     * if the API response isn't empty
     * @throws IOException              if there's an unexpected HTTP status code (less than
     *                                  200 or greater than 299) or if there's a problem reading the
     *                                  API response
     * @throws NullPointerException     if {@code account} is {@code null}
     * @throws NullPointerException     if {@code startTime} is {@code null}
     * @throws NullPointerException     if {@code endTime} is {@code null}
     * @throws IllegalArgumentException if {@code startTime} is greater than or equal to {@code endTime}
     * @since 3.1.0
     */
    default Optional<FilterableStatisticV2> findAllByAccount(final Account account,
                                                             final ZonedDateTime startTime,
                                                             final ZonedDateTime endTime) throws IOException {
        Objects.requireNonNull(account, "account cannot be null");
        return findAllByAccountId(account.accountId(), startTime, endTime);
    }

    /**
     * @param startTime start of the time window
     * @param endTime   end of the time window
     * @return an {@link Optional} of {@link FilterableStatisticV2} that's non-empty
     * if the API response isn't empty
     * @throws IOException              if there's an unexpected HTTP status code (less than
     *                                  200 or greater than 299) or if there's a problem reading the
     *                                  API response
     * @throws NullPointerException     if {@code startTime} is {@code null}
     * @throws NullPointerException     if {@code endTime} is {@code null}
     * @throws IllegalArgumentException if {@code startTime} is greater than or equal to {@code endTime}
     * @since 3.1.0
     */
    default Optional<FilterableStatisticV2> findAllBySessionAccountId(final ZonedDateTime startTime,
                                                                      final ZonedDateTime endTime) throws IOException {
        return Optional.empty();
    }
}
