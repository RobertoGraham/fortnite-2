package io.github.robertograham.fortnite2.domain;

import java.time.LocalDateTime;

/**
 * Provides statistics
 *
 * @since 1.0.0
 */
public interface Statistic {

    /**
     * @return number of wins
     * @since 1.0.0
     */
    long wins();

    /**
     * @return number of matches played
     * @since 1.0.0
     */
    long matches();

    /**
     * @return number of kills
     * @since 1.0.0
     */
    long kills();

    /**
     * @return account score
     * @since 1.0.0
     */
    long score();

    /**
     * @return number of top 10 finishes. 0 for non solo-scoped
     * @since 1.0.0
     */
    long timesPlacedTop10();

    /**
     * @return number of top 25 finishes. 0 for non solo-scoped
     * @since 1.0.0
     */
    long timesPlacedTop25();

    /**
     * @return number of top 5 finishes. 0 for non duo-scoped
     * @since 1.0.0
     */
    long timesPlacedTop5();

    /**
     * @return number of top 12 finishes. 0 for non duo-scoped
     * @since 1.0.0
     */
    long timesPlacedTop12();

    /**
     * @return number of top 3 finishes. 0 for non squad-scoped
     * @since 1.0.0
     */
    long timesPlacedTop3();

    /**
     * @return number of top 6 finishes. 0 for non squad-scoped
     * @since 1.0.0
     */
    long timesPlacedTop6();

    /**
     * @return time this statistic was last updated
     * @since 1.1.0
     */
    LocalDateTime timeLastModified();

    /**
     * @return the number of players that died before the user did
     * @since 3.1.0
     */
    default long playersOutlived() {
        return 0L;
    }

    /**
     * @return the number of minutes the user has played Fortnite
     * @since 3.1.0
     */
    default long minutesPlayed() {
        return 0L;
    }
}
