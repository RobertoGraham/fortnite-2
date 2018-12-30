package io.github.robertograham.fortnite2.resource;

import io.github.robertograham.fortnite2.domain.Account;
import io.github.robertograham.fortnite2.domain.FilterableStatistic;

import java.io.IOException;
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
    Optional<FilterableStatistic> findAllByAccountIdForAllTime(String accountId) throws IOException;

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
    default Optional<FilterableStatistic> findAllByAccountForAllTime(Account account) throws IOException {
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
    default Optional<FilterableStatistic> findAllBySessionAccountIdForAllTime() throws IOException {
        return Optional.empty();
    }

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
    Optional<FilterableStatistic> findAllByAccountIdForCurrentSeason(String accountId) throws IOException;

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
    default Optional<FilterableStatistic> findAllByAccountForCurrentSeason(Account account) throws IOException {
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
    default Optional<FilterableStatistic> findAllBySessionAccountIdForCurrentSeason() throws IOException {
        return Optional.empty();
    }
}
