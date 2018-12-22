package io.github.robertograham.fortniteclienttwo.implementation;

import io.github.robertograham.fortniteclienttwo.domain.Statistic;

enum EmptyStatistic implements Statistic {

    INSTANCE;

    @Override
    public long wins() {
        return 0L;
    }

    @Override
    public long matches() {
        return 0L;
    }

    @Override
    public long kills() {
        return 0L;
    }

    @Override
    public long score() {
        return 0L;
    }

    @Override
    public long timesPlacedTop10() {
        return 0L;
    }

    @Override
    public long timesPlacedTop25() {
        return 0L;
    }

    @Override
    public long timesPlacedTop5() {
        return 0L;
    }

    @Override
    public long timesPlacedTop12() {
        return 0L;
    }

    @Override
    public long timesPlacedTop3() {
        return 0L;
    }

    @Override
    public long timesPlacedTop6() {
        return 0L;
    }
}
