package io.github.robertograham.fortnite2.domain;

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
}
