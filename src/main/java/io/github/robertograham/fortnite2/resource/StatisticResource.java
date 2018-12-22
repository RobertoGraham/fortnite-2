package io.github.robertograham.fortnite2.resource;

import io.github.robertograham.fortnite2.domain.Account;
import io.github.robertograham.fortnite2.domain.FilterableStatistic;

import java.io.IOException;
import java.util.Optional;

public interface StatisticResource {

    Optional<FilterableStatistic> findAllByAccountIdForAllTime(String accountId) throws IOException;

    Optional<FilterableStatistic> findAllByAccountForAllTime(Account account) throws IOException;

    Optional<FilterableStatistic> findAllByAccountIdForCurrentSeason(String accountId) throws IOException;

    Optional<FilterableStatistic> findAllByAccountForCurrentSeason(Account account) throws IOException;
}
