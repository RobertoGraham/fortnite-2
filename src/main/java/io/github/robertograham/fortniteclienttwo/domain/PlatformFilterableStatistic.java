package io.github.robertograham.fortniteclienttwo.domain;

import io.github.robertograham.fortniteclienttwo.domain.enumeration.Platform;

public interface PlatformFilterableStatistic extends Statistic {

    Statistic byPlatform(Platform platform);
}
