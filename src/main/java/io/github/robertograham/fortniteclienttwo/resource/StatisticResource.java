package io.github.robertograham.fortniteclienttwo.resource;

import io.github.robertograham.fortniteclienttwo.domain.Account;
import io.github.robertograham.fortniteclienttwo.domain.FilterableStatistic;

import java.io.IOException;
import java.util.Optional;

public interface StatisticResource {

    Optional<FilterableStatistic> findAllByAccountIdForAllTime(String accountId) throws IOException;

    Optional<FilterableStatistic> findAllByAccountForAllTime(Account account) throws IOException;

    Optional<FilterableStatistic> findAllByAccountIdForCurrentSeason(String accountId) throws IOException;

    Optional<FilterableStatistic> findAllByAccountForCurrentSeason(Account account) throws IOException;
}
