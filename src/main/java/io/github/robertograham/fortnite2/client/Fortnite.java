package io.github.robertograham.fortnite2.client;

import io.github.robertograham.fortnite2.domain.Token;
import io.github.robertograham.fortnite2.resource.AccountResource;
import io.github.robertograham.fortnite2.resource.FriendResource;
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

    /**
     * @return an object from which friend related API endpoints can be called
     * @since 1.3.0
     */
    FriendResource friend();

    /**
     * The returned {@link Token} is guaranteed to be a non-expired one
     *
     * @return the {@link Token} that represents the current session
     * @since 2.1.0
     */
    default Token session() {
        return null;
    }
}
