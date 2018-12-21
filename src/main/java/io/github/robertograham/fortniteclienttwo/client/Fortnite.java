package io.github.robertograham.fortniteclienttwo.client;

import io.github.robertograham.fortniteclienttwo.resource.AccountResource;
import io.github.robertograham.fortniteclienttwo.resource.LeaderBoardResource;
import io.github.robertograham.fortniteclienttwo.resource.StatisticResource;

public interface Fortnite extends AutoCloseable {

    @Override
    void close();

    AccountResource account();

    LeaderBoardResource leaderBoard();

    StatisticResource statistic();
}
