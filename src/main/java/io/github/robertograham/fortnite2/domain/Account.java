package io.github.robertograham.fortnite2.domain;

/**
 * Representation of an Epic Games account
 *
 * @since 1.0.0
 */
public interface Account {

    /**
     * @return ID of this Epic Games account
     * @since 1.0.0
     */
    String accountId();

    /**
     * @return username of this Epic Games account
     * @since 1.0.0
     */
    String displayName();
}
