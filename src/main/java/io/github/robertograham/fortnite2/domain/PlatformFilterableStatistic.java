package io.github.robertograham.fortnite2.domain;

import io.github.robertograham.fortnite2.domain.enumeration.Platform;

/**
 * A {@link Statistic} that can be filtered by {@link Platform}
 *
 * @since 1.0.0
 */
public interface PlatformFilterableStatistic extends Statistic {

    /**
     * @param platform the platform to filter this statistic by
     * @return a {@link Statistic} with values scoped to {@code platform}
     * @since 1.0.0
     */
    Statistic byPlatform(final Platform platform);
}
