package io.github.robertograham.fortnite2.domain;

import io.github.robertograham.fortnite2.domain.enumeration.Platform;

public interface PlatformFilterableStatistic extends Statistic {

    Statistic byPlatform(Platform platform);
}
