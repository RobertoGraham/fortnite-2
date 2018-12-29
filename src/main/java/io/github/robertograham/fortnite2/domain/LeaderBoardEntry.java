package io.github.robertograham.fortnite2.domain;

/**
 * A leader board entry
 *
 * @since 1.0.0
 */
public interface LeaderBoardEntry {

    /**
     * @return the ID the Epic Games account this entry represents. The ID may
     * contain the following character: '{@code -}'
     * @since 1.0.0
     */
    String accountId();

    /**
     * @return typically the number of wins
     * @since 1.0.0
     */
    long value();
}
