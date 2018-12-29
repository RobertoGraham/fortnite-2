package io.github.robertograham.fortnite2.client;

import io.github.robertograham.fortnite2.resource.AccountResource;
import io.github.robertograham.fortnite2.resource.LeaderBoardResource;
import io.github.robertograham.fortnite2.resource.StatisticResource;

/**
 * An object from which all of the supported Epic Games Launcher and Fortnite
 * Client API endpoints can be called
 *
 * @since 1.0.0
 */
public interface Fortnite extends AutoCloseable {

    @Override
    void close();

    /**
     * @return an object from which account related API endpoints can be called
     * @since 1.0.0
     */
    AccountResource account();

    /**
     * @return an object from which leader board related API endpoints can be called
     * @since 1.0.0
     */
    LeaderBoardResource leaderBoard();

    /**
     * @return an object from which statistic related API endpoints can be called
     * @since 1.0.0
     */
    StatisticResource statistic();
}
