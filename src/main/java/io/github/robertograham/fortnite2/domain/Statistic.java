package io.github.robertograham.fortnite2.domain;

import java.time.LocalDateTime;

public interface Statistic {

    long wins();

    long matches();

    long kills();

    long score();

    /**
     * @return 0 for non-solo
     */
    long timesPlacedTop10();

    /**
     * @return 0 for non-solo
     */
    long timesPlacedTop25();

    /**
     * @return 0 for non-duo
     */
    long timesPlacedTop5();

    /**
     * @return 0 for non-duo
     */
    long timesPlacedTop12();

    /**
     * @return 0 for non-squad
     */
    long timesPlacedTop3();

    /**
     * @return 0 for non-squad
     */
    long timesPlacedTop6();

    /**
     * @return LocalDateTime.MIN by default.
     * Default so as not to break implementations of version 1.x.x of this interface
     * Will no longer be default in version 2.x.x of this interface
     */
    default LocalDateTime timeLastModified() {
        return LocalDateTime.MIN;
    }
}
