package io.github.robertograham.fortnite2.client;

import io.github.robertograham.fortnite2.resource.AccountResource;
import io.github.robertograham.fortnite2.resource.LeaderBoardResource;
import io.github.robertograham.fortnite2.resource.StatisticResource;

public interface Fortnite extends AutoCloseable {

    @Override
    void close();

    AccountResource account();

    LeaderBoardResource leaderBoard();

    StatisticResource statistic();
}
